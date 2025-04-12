package com.hering.desafiojava.core.repositories;

import com.hering.desafiojava.core.entities.TextToSpeech;
import com.hering.desafiojava.core.entities.TextToSpeechStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextToSpeechRepository extends JpaRepository<TextToSpeech, Long> {
    Page<TextToSpeech> findByStatusIn(Pageable pageable,List<TextToSpeechStatus> status);
}