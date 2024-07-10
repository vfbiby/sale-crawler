package com.muhuang.salecrawler.rate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/NotesRate")
public class NotesRateController {

    private final NotesRateRepository notesRateRepository;

    public NotesRateController(NotesRateRepository notesRateRepository) {
        this.notesRateRepository = notesRateRepository;
    }

    @PostMapping
    void createNotesRate(@RequestBody NotesRate notesRate) {
        notesRateRepository.save(notesRate);
    }

}
