package com.hering.desafiojava.api.controller;

import com.hering.desafiojava.api.services.TextToSpeechProducerService;
import com.hering.desafiojava.core.entities.TextToSpeechStatus;
import com.hering.desafiojava.core.services.model.TextToSpeechModel;
import com.hering.desafiojava.core.services.TextToSpeechService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private TextToSpeechProducerService kafkaService;

    public TextToSpeechController(TextToSpeechService service, TextToSpeechProducerService kafkaService) {
        this.service = service;
        this.kafkaService = kafkaService;
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

    @GetMapping()
    public ResponseEntity<Page<TextToSpeechModel>> list(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                        @RequestParam(name = "status", required = false) List<TextToSpeechStatus> statusList){
        return ResponseEntity.ok().body(service.list(pageable, statusList));
    }


    @GetMapping("async")
    public TextToSpeechModel createAsync(@RequestParam("text") String text, @RequestParam("language") String language, @RequestParam("voice") String voice){
        TextToSpeechModel model = service.createSpeechAsync(language,voice,text);
        kafkaService.sendIdToQueue(model.getId());
        return model;
    }
}
