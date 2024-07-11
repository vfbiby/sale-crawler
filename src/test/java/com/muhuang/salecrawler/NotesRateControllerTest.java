package com.muhuang.salecrawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhuang.salecrawler.rate.*;
import jakarta.annotation.Resource;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

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

    @Autowired
    private ServletWebServerApplicationContext webServerApplicationContext;

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
        notesRate.setNoteType(List.of(NoteType.builder().contentTag("fashion").percent(88.2).build()));
        ResponseEntity<NotesRate> response = postNotesRate(notesRate);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getNoteType().get(0).getContentTag()).isEqualTo("fashion");
    }

    @Test
    @DisplayName("three CamelCase property should use JsonProperty annotation on entity field")
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithMEngagementNum() {
        //if you construct an object to post, it will success when testing, but failed at product environment
        String jsonToPost = """
                {
                    "mEngagementNum": 1711
                  }""";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonToPost, httpHeaders);
        int port = webServerApplicationContext.getWebServer().getPort();
        NotesRate response = new TestRestTemplate().postForObject("http://localhost:" + port + "/api/1.0/NotesRate", stringHttpEntity, NotesRate.class);
        assertThat(response.getMEngagementNum()).isEqualTo(1711);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithMFollowCnt() {
        String jsonToPost = """
                {
                    "mFollowCnt": 172
                  }""";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonToPost, httpHeaders);
        int port = webServerApplicationContext.getWebServer().getPort();
        NotesRate response = new TestRestTemplate().postForObject("http://localhost:" + port + "/api/1.0/NotesRate", stringHttpEntity, NotesRate.class);
        assertThat(response.getMFollowCnt()).isEqualTo(172);
    }

    private ResponseEntity<NotesRate> postNotesRate(NotesRate notesRate) {
        return testRestTemplate.postForEntity("/api/1.0/NotesRate", notesRate, NotesRate.class);
    }

}
