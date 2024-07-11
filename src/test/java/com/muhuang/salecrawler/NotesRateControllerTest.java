package com.muhuang.salecrawler;

import com.muhuang.salecrawler.rate.*;
import jakarta.annotation.Resource;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.time.LocalDate;
import java.util.List;
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

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithNoteType() {
        NotesRate notesRate = new NotesRate();
        notesRate.setNoteTypeList(List.of(NoteType.builder().contentTag("fashion").percent(88.2).build()));
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getNoteTypeList().get(0).getContentTag()).isEqualTo("fashion");
    }

    private ResponseEntity<NotesRate> postNotesRate(NotesRate notesRate) {
        return testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, NotesRate.class);
    }

}
