package com.hering.desafiojava.core.services;

import com.hering.desafiojava.core.entities.TextToSpeech;
import com.hering.desafiojava.core.entities.TextToSpeechStatus;
import com.hering.desafiojava.core.services.model.TextToSpeechModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TextToSpeechService {

    private  static final Map<Long, TextToSpeech> ENTITIES = new ConcurrentHashMap<>();

    public TextToSpeechModel createSpeech(String language, String voice, String text){

        final HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String url = "http://api.voicerss.org/?key=b52ad1e1a7ea44e799babb04ff4d229f&hl=" + language + "&v=" + voice + "&src=" + encodeValue(text);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
    //                .header("AuthenticationToken", apiUserToken)
                    .GET()
                    .build();

            var httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if(httpResponse.statusCode() != 200){
                return null;
            }

            var model = new TextToSpeechModel();

            model.setMomment(Instant.now());
            model.setLanguage(language);
            model.setVoice(voice);
            model.setText(text);
            model.setStatus(TextToSpeechStatus.COMPLETED);

            // TODO criar uma persistência em banco, pode ser SQLite
            var entity = model.toEntity();
            entity.setId(ENTITIES.keySet().size() + 1L);
            model.setId(entity.getId());

            InputStream is = httpResponse.body();
            byte[] bytes = is.readAllBytes();
            String encoded = Base64.getEncoder().encodeToString(bytes);
            entity.setAudioWavBase64(encoded);

            ENTITIES.put(entity.getId(), entity);

            return model;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public TextToSpeechModel search(Long id) {
        var entity = ENTITIES.get(id);

        if(entity == null){
            return null;
        }

        return TextToSpeechModel.fromEntity(entity);
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] searchAudio(Long id) {
        var entity = ENTITIES.get(id);

        if(entity == null){
            return null;
        }

        return Base64.getDecoder().decode(entity.getAudioWavBase64());
    }
}
