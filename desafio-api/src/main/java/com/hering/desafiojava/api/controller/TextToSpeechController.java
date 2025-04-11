package com.hering.desafiojava.api.controller;

import com.hering.desafiojava.core.services.model.TextToSpeechModel;
import com.hering.desafiojava.core.services.TextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("v1/text-to-speech")
public class TextToSpeechController {

    private TextToSpeechService service;

    public TextToSpeechController(TextToSpeechService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Object byId(@PathVariable("id") Long id){
        return service.search(id);
    }

    @GetMapping("{id}/audio")
    public ResponseEntity<byte[]> byAudioId(@PathVariable("id") Long id){
        byte[] bytes = service.searchAudio(id);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/wav"));
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("sync")
    public TextToSpeechModel createSync(@RequestParam("text") String text, @RequestParam("language") String language, @RequestParam("voice") String voice){

        TextToSpeechModel model = service.createSpeech(language,voice,text);

        return model;
    }

}
