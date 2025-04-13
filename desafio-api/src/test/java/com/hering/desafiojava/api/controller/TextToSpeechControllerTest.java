package com.hering.desafiojava.api.controller;

import com.hering.desafiojava.api.common.Constants;
import com.hering.desafiojava.api.services.TextToSpeechProducerService;
import com.hering.desafiojava.common.exceptions.BusinessException;
import com.hering.desafiojava.core.entities.TextToSpeechStatus;
import com.hering.desafiojava.core.services.TextToSpeechService;
import com.hering.desafiojava.core.services.model.TextToSpeechModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TextToSpeechControllerTest {

    @Mock
    private TextToSpeechService service;

    @Mock
    private TextToSpeechProducerService kafkaService;

    @InjectMocks
    private TextToSpeechController controller;

    @Test
    public void testCreateAsyncRequest() {
        TextToSpeechModel mockModel = Constants.TEXT_TO_SPEECH_MODEL_PROCESSING;
        when(service.createSpeechAsync("pt-br", "Dinis", "Olá, Bom dia")).thenReturn(mockModel);
        doNothing().when(kafkaService).sendIdToQueue(mockModel.getId());

        TextToSpeechModel response = controller.createAsync("Olá, Bom dia", "pt-br", "Dinis");

        assertEquals(1L, response.getId());
        assertEquals(TextToSpeechStatus.PROCESSING, response.getStatus());
        assertEquals("Olá, Bom dia", response.getText());
        assertEquals("Dinis", response.getVoice());
        assertEquals("pt-br", response.getLanguage());
        assertNotNull(response.getMomment());

        verify(kafkaService, times(1)).sendIdToQueue(mockModel.getId());
    }

    @Test
    public void testById() {
        Long id = 1L;
        TextToSpeechModel mockModel = Constants.TEXT_TO_SPEECH_MODEL_COMPLETED;
        when(service.search(id)).thenReturn(mockModel);

        TextToSpeechModel result = (TextToSpeechModel) controller.byId(id);

        assertEquals(1L, result.getId());
        assertEquals(TextToSpeechStatus.COMPLETED, result.getStatus());
    }


    @Test
    public void testByAudioIdProcessing() {
        Long id = 1L;

        ResponseEntity response = ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Audio ainda em processamento");

        when(service.searchAudio(id)).thenReturn(response);

        ResponseEntity<?> result = controller.byAudioId(id);

        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals("Audio ainda em processamento", result.getBody());
    }


    @Test
    public void testByAudioIdProcessed() {
        Long id = 1L;
        byte[] fakeAudio = "audio-fake".getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/wav"));
        ResponseEntity response = new ResponseEntity(fakeAudio, headers, HttpStatus.OK);

        when(service.searchAudio(id)).thenReturn(response);

        ResponseEntity<?> result = controller.byAudioId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.parseMediaType("audio/wav"), result.getHeaders().getContentType());
        assertArrayEquals(fakeAudio, (byte[]) result.getBody());
    }


    @Test
    public void testByAudioIdFailed() {
        Long id = 1L;

        when(service.searchAudio(id)).thenThrow(new BusinessException("Falha no processamento do áudio"));

        BusinessException exception = assertThrows(BusinessException.class, () -> controller.byAudioId(id));

        assertEquals("Falha no processamento do áudio", exception.getMessage());
    }


    @Test
    public void testList() {
        Pageable pageable = mock(Pageable.class);
        List<TextToSpeechStatus> statusList = List.of(TextToSpeechStatus.COMPLETED);
        Page<TextToSpeechModel> page = mock(Page.class);
        when(service.list(pageable, statusList)).thenReturn(page);

        ResponseEntity<Page<TextToSpeechModel>> response = controller.list(pageable, statusList);

        assertEquals(page, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }




}