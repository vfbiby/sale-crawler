package com.muhuang.salecrawler;

import com.muhuang.salecrawler.rate.LongTermCommonNoteVo;
import com.muhuang.salecrawler.rate.NotesRate;
import com.muhuang.salecrawler.rate.NotesRateRepository;
import com.muhuang.salecrawler.rate.PagePercentVo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NotesRateControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private NotesRateRepository notesRateRepository;

    @Test
    void postNotesRate_whenNotesRateIsValid_receiveOK() {
        NotesRate notesRate = new NotesRate();
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NotesRateSaveToDatabase() {
        NotesRate notesRate = new NotesRate();
        notesRate.setNoteNumber(33);
        notesRate.setVideoNoteNumber(22);
        notesRate.setHundredLikePercent(84.8);
        testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, Object.class);
        assertThat(notesRateRepository.findAll()).hasSize(1);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithPagePercentVo() {
        NotesRate notesRate = new NotesRate();
        PagePercentVo pagePercentVo = PagePercentVo.builder().build();
        notesRate.setPagePercentVo(pagePercentVo);
        testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, Object.class);
        List<NotesRate> noteRates = notesRateRepository.findAll();
        assertThat(noteRates.get(0).getPagePercentVo()).isNotNull();
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithLongTermCommonNoteVo() {
        NotesRate notesRate = new NotesRate();
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, Object.class);
        List<NotesRate> noteRates = notesRateRepository.findAll();
        assertThat(noteRates.get(0).getLongTermCommonNoteVo()).isNotNull();
    }

}
