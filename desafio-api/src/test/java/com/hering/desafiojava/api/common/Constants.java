package com.hering.desafiojava.api.common;

import com.hering.desafiojava.core.entities.TextToSpeechStatus;
import com.hering.desafiojava.core.services.model.TextToSpeechModel;

import java.time.Instant;

public class Constants {
    public static final TextToSpeechModel TEXT_TO_SPEECH_MODEL_PROCESSING = new TextToSpeechModel(1L, Instant.now(), "pt-br", "Dinis", "Olá, Bom dia", TextToSpeechStatus.PROCESSING);
    public static final TextToSpeechModel TEXT_TO_SPEECH_MODEL_COMPLETED = new TextToSpeechModel(1L, Instant.now(), "pt-br", "Dinis", "Olá, Bom dia", TextToSpeechStatus.COMPLETED);
    public static final TextToSpeechModel TEXT_TO_SPEECH_MODEL_FAILED = new TextToSpeechModel(1L, Instant.now(), "pt-br", "Dinis", "Olá, Bom dia", TextToSpeechStatus.ERROR);
}
