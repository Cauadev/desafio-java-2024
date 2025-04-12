package com.hering.desafiojava.core.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
public class TextToSpeech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant momment;

    private String language;

    private String voice;

    private String text;

    @Enumerated(EnumType.STRING)
    private TextToSpeechStatus status;

    private String errorText;

    private String audioWavBase64;
}
