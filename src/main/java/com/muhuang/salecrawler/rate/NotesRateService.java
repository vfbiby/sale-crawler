package com.muhuang.salecrawler.rate;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotesRateService {

    @Resource
    private NotesRateRepository notesRateRepository;

    NotesRate save(NotesRate notesRate) {
        String uniqueNotesRateId = notesRate.generateUniqueNotesRateId();
        Optional<NotesRate> byUniqueNotesRateId = notesRateRepository.findByUniqueNotesRateId(uniqueNotesRateId);
        if (byUniqueNotesRateId.isPresent()){
            throw new NotesRateExistException();
        }
        return notesRateRepository.save(notesRate);
    }
}