package com.muhuang.salecrawler.note;

import com.muhuang.salecrawler.rate.*;
import com.muhuang.salecrawler.shared.ApiError;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NotesRateControllerTest {

    public static final String API_1_0_NOTES_RATE = "/api/1.0/NotesRate";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private NotesRateRepository notesRateRepository;

    @Autowired
    private ServletWebServerApplicationContext webServerApplicationContext;

    @BeforeEach
    void cleanup() {
        notesRateRepository.deleteAll();
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_receiveOK() {
        NotesRate notesRate = getNotesRate();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_NOTES_RATE, notesRate, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NotesRateSaveToDatabase() {
        NotesRate notesRate = getNotesRate();
        postNotesRate(notesRate, NotesRate.class);
        assertThat(notesRateRepository.findAll()).hasSize(1);
    }

    private static NotesRate getNotesRate() {
        NotesRate notesRate = new NotesRate();
        notesRate.setNoteNumber(33);
        notesRate.setVideoNoteNumber(22);
        notesRate.setHundredLikePercent(84.8);
        notesRate.setUserId("5bb0275645c6e8000154f64c");
        notesRate.setType(NotesType.D30);
        return notesRate;
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithPagePercentVo() {
        NotesRate notesRate = getNotesRate();
        notesRate.setPagePercentVo(PagePercentVo.builder().build());
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getPagePercentVo()).isNotNull();
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_pagePercentVoFieldsSaveToDatabase() {
        NotesRate notesRate = getNotesRate();
        PagePercentVo pagePercentVo = PagePercentVo.builder().impHomefeedPercent(88.2).build();
        notesRate.setPagePercentVo(pagePercentVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getPagePercentVo().getImpHomefeedPercent()).isEqualTo(88.2);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithLongTermCommonNoteVo() {
        NotesRate notesRate = getNotesRate();
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getLongTermCommonNoteVo()).isNotNull();
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_longTermCommonNoteVoFieldsSaveToDatabase() {
        NotesRate notesRate = getNotesRate();
        LocalDate now = LocalDate.now();
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().startPublishTime(now).build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getLongTermCommonNoteVo().getStartPublishTime()).isEqualTo(now);
    }

    @Test
    void postNotesRate_whenNotesRateHasAFieldInChildrenEntity_thisFieldNotSetToChildrenField() {
        NotesRate notesRate = getNotesRate();
        notesRate.setNoteNumber(33);
        LongTermCommonNoteVo longTermCommonNoteVo = LongTermCommonNoteVo.builder().noteNumber(44).build();
        notesRate.setLongTermCommonNoteVo(longTermCommonNoteVo);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getLongTermCommonNoteVo().getNoteNumber()).isEqualTo(44);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithNoteType() {
        NotesRate notesRate = getNotesRate();
        notesRate.setNoteType(List.of(NoteType.builder().contentTag("fashion").percent(88.2).build()));
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        Optional<NotesRate> noteRates = notesRateRepository.findById(response.getBody().getId());
        assertThat(noteRates.get().getNoteType().get(0).getContentTag()).isEqualTo("fashion");
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithCalculatedProperty() {
        NotesRate notesRate = getNotesRate();
        notesRate.setUserId("5bb0275645c6e8000154f64c");
        notesRate.setCaptureDate(LocalDate.parse("2024-07-12"));
        notesRate.setType(NotesType.D30);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        assertThat(response.getBody().getUniqueNotesRateId()).isEqualTo("5bb0275645c6e8000154f64c2024-07-12D30");
    }

    @Test
    @DisplayName("three CamelCase property should use JsonProperty annotation on entity field")
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithMEngagementNum() {
        //if you construct an object to post, it will success when testing, but failed at product environment
        String jsonToPost = """
                {
                    "userId": "5bb0275645c6e8000154f64c",
                    "type": "D30",
                    "mEngagementNum": 1711
                  }""";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonToPost, httpHeaders);
        int port = webServerApplicationContext.getWebServer().getPort();
        String url = "http://localhost:" + port + API_1_0_NOTES_RATE;
        NotesRate response = new TestRestTemplate().postForObject(url, stringHttpEntity, NotesRate.class);
        assertThat(response.getMEngagementNum()).isEqualTo(1711);
    }

    @Test
    void postNotesRate_whenNotesRateIsValid_NoteRateSaveToDatabaseWithMFollowCnt() {
        String jsonToPost = """
                {
                    "userId": "5bb0275645c6e8000154f64c",
                    "type": "D30",
                    "mFollowCnt": 172
                  }""";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonToPost, httpHeaders);
        int port = webServerApplicationContext.getWebServer().getPort();
        String url = "http://localhost:" + port + API_1_0_NOTES_RATE;
        NotesRate response = new TestRestTemplate().postForObject(url, stringHttpEntity, NotesRate.class);
        assertThat(response.getMFollowCnt()).isEqualTo(172);
    }

    @Test
    void postNotesRate_whenNotesRateIsExist_receiveBadRequest() {
        NotesRate notesRate = getNotesRate();
        notesRateRepository.save(notesRate);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void postNotesRate_whenNotesRateHasNullUserId_receiveBadRequest() {
        NotesRate notesRate = getNotesRate();
        notesRate.setUserId(null);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void postNotesRate_whenNotesRateHasNullType_receiveBadRequest() {
        NotesRate notesRate = getNotesRate();
        notesRate.setType(null);
        ResponseEntity<NotesRate> response = postNotesRate(notesRate, NotesRate.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void postNotesRate_whenNotesRateIsExist_receiveApiError() {
        NotesRate notesRate = getNotesRate();
        notesRateRepository.save(notesRate);
        ResponseEntity<ApiError> response = postNotesRate(notesRate, ApiError.class);
        assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_NOTES_RATE);
    }

    @Test
    void postNotesRate_whenNotesRateIsExist_receiveApiErrorWithValidationErrors() {
        NotesRate notesRate = getNotesRate();
        notesRateRepository.save(notesRate);
        ResponseEntity<ApiError> response = postNotesRate(notesRate, ApiError.class);
        assertThat(response.getBody().getValidationErrors().size()).isEqualTo(1);
    }

    @Test
    void postNotesRate_whenNotesRateIsExist_receiveMessageOfExistingNotesRate() {
        NotesRate notesRate = getNotesRate();
        notesRateRepository.save(notesRate);
        ResponseEntity<ApiError> response = postNotesRate(notesRate, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertThat(validationErrors.get("NotesRate")).isEqualTo("NotesRate of today exist!");
    }

    @Test
    void postNotesRate_whenNotesRateHasNullUserId_receiveMessageOfNullErrorsForUserId() {
        NotesRate notesRate = getNotesRate();
        notesRate.setUserId(null);
        ResponseEntity<ApiError> response = postNotesRate(notesRate, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertThat(validationErrors.get("userId")).isEqualTo("koc userId must not be null");
    }

    private <T> ResponseEntity<T> postNotesRate(NotesRate notesRate, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_NOTES_RATE, notesRate, responseType);
    }

}
