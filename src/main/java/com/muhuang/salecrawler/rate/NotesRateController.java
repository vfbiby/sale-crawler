package com.muhuang.salecrawler.rate;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1.0/NotesRate")
@CrossOrigin
public class NotesRateController {

    private final NotesRateRepository notesRateRepository;

    public NotesRateController(NotesRateRepository notesRateRepository) {
        this.notesRateRepository = notesRateRepository;
    }

    @PostMapping
    NotesRate createNotesRate(@RequestBody NotesRate notesRate) {
        System.out.println(notesRate);
        return notesRateRepository.save(notesRate);
    }

}
