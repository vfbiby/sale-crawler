package com.muhuang.salecrawler.rate;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1.0/NotesRate")
@CrossOrigin
public class NotesRateController {

    @Resource
    private NotesRateService notesRateService;

    @PostMapping
    NotesRate createNotesRate(@RequestBody NotesRate notesRate) {
        return notesRateService.save(notesRate);
    }

}
