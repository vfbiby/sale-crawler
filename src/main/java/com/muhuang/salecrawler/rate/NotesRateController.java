package com.muhuang.salecrawler.rate;

import org.modelmapper.ModelMapper;
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
    NotesRate createNotesRate(@RequestBody NotesRate notesRate) {
        System.out.println(notesRate);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(NotesRate.class, LongTermCommonNoteVo.class).addMapping(
                src -> src.getLongTermCommonNoteVo().getStartPublishTime(), LongTermCommonNoteVo::setStartPublishTime);
        PagePercentVo pagePercentVo = modelMapper.map(notesRate, PagePercentVo.class);
        LongTermCommonNoteVo longTermCommonNoteVo = modelMapper.map(notesRate, LongTermCommonNoteVo.class);
        notesRate.setPagePercentVo(pagePercentVo);
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        return notesRateRepository.save(notesRate);
    }

}
