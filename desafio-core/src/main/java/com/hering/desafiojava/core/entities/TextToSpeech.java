package com.hering.desafiojava.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TextToSpeech {
    private Long id;
    private Instant momment;
    private String language;
    private String voice;
    private String text;
    private TextToSpeechStatus status;
    private String errorText;
    private String audioWavBase64;
}
