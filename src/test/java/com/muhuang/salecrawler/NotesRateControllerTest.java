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
import java.util.Optional;

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
        postNotesRate(notesRate);
        assertThat(notesRateRepository.findAll()).hasSize(1);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithPagePercentVo() {
        NotesRate notesRate = new NotesRate();
        notesRate.setPagePercentVo(PagePercentVo.builder().build());
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getPagePercentVo()).isNotNull();
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_pagePercentVoFieldsSaveToDatabase() {
        NotesRate notesRate = new NotesRate();
        PagePercentVo pagePercentVo = PagePercentVo.builder().impHomefeedPercent(88.2).build();
        notesRate.setPagePercentVo(pagePercentVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getPagePercentVo().getImpHomefeedPercent()).isEqualTo(88.2);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithLongTermCommonNoteVo() {
        NotesRate notesRate = new NotesRate();
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getLongTermCommonNoteVo()).isNotNull();
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_longTermCommonNoteVoFieldsSaveToDatabase() {
        NotesRate notesRate = new NotesRate();
        LocalDate now = LocalDate.now();
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().startPublishTime(now).build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getLongTermCommonNoteVo().getStartPublishTime()).isEqualTo(now);
    }

    @Test
    void postNotesRate_whenNotesRateHasAFieldInChildrenEntity_thisFieldNotSetToChildrenField() {
        NotesRate notesRate = new NotesRate();
        notesRate.setNoteNumber(33);
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().noteNumber(44).build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getLongTermCommonNoteVo().getNoteNumber()).isEqualTo(44);
    }

    private ResponseEntity<NotesRate> postNotesRate(NotesRate notesRate) {
        return testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, NotesRate.class);
    }

}