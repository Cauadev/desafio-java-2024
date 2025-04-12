package com.hering.desafiojava.core.services;

import com.hering.desafiojava.core.entities.TextToSpeech;
import com.hering.desafiojava.core.entities.TextToSpeechStatus;
import com.hering.desafiojava.core.repositories.TextToSpeechRepository;
import com.hering.desafiojava.core.services.model.TextToSpeechModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.stream.Collectors;

@Service
public class TextToSpeechService {

    private final TextToSpeechRepository repository;

    public TextToSpeechService(TextToSpeechRepository repository) {
        this.repository = repository;
    }

    @Transactional
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

            var entity = model.toEntity();
            InputStream is = httpResponse.body();
            byte[] bytes = is.readAllBytes();
            String encoded = Base64.getEncoder().encodeToString(bytes);
            entity.setAudioWavBase64(encoded);

            repository.save(entity);

            model.setId(entity.getId());
            return model;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public TextToSpeechModel createSpeechAsync(String language, String voice, String text){
            var model = new TextToSpeechModel();

            model.setMomment(Instant.now());
            model.setLanguage(language);
            model.setVoice(voice);
            model.setText(text);
            model.setStatus(TextToSpeechStatus.PROCESSING);

            var entity = model.toEntity();
            repository.save(entity);

            model.setId(entity.getId());
            return model;
    }


    @Transactional
    public void requestVoiceRSSAndUpdate(Long textToSpeechId){

        var entity = repository.findById(textToSpeechId).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        final HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String url = "http://api.voicerss.org/?key=b52ad1e1a7ea44e799babb04ff4d229f&hl=" + entity.getLanguage() + "&v=" + entity.getVoice() + "&src=" + encodeValue(entity.getText());

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            var httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if(httpResponse.statusCode() != 200){
                entity.setStatus(TextToSpeechStatus.ERROR);
                entity.setErrorText(httpResponse.body().toString());
                return;
            }

            InputStream is = httpResponse.body();
            byte[] bytes = is.readAllBytes();
            String encoded = Base64.getEncoder().encodeToString(bytes);
            entity.setAudioWavBase64(encoded);
            entity.setStatus(TextToSpeechStatus.COMPLETED);

            repository.save(entity);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public TextToSpeechModel search(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
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
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        return Base64.getDecoder().decode(entity.getAudioWavBase64());
    }

    public Page<TextToSpeechModel> list(Pageable pageable, List<TextToSpeechStatus> statusList) {
        Page<TextToSpeech> page;

        if (statusList == null || statusList.isEmpty()) {
            page = repository.findAll(pageable);
        } else {
            page = repository.findByStatusIn(pageable, statusList);
        }

        return page.map(TextToSpeechModel::fromEntity);
    }
}
