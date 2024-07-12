package com.muhuang.salecrawler.schedule;

import com.muhuang.salecrawler.schedule.Schedule;
import com.muhuang.salecrawler.schedule.ScheduleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostScheduleControllerTest {

    public static final String API_1_0_SCHEDULES = "/api/1.0/schedules";

    @Resource
    private TestRestTemplate restTestTemplate;

    @Resource
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    void cleanup() {
        scheduleRepository.deleteAll();
    }

    @Test
    void postSchedule_whenItemIsValid_receiveOK() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId("3423423");
        ResponseEntity<Object> response = postSchedule(schedule);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postSchedule_whenItemIsValid_itemSaveToDatabase() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId("34234324");
        postSchedule(schedule);
        assertThat(scheduleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void postSchedule_whenItemIsValid_schedulesStatusIsPending() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId("34234324");
        postSchedule(schedule);
        assertThat(scheduleRepository.findByOutItemId("34234324").getStatus()).isEqualTo("pending");
    }

    @Test
    void postSchedule_whenScheduleHasNullItemId_receiveBadRequest() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId(null);
        ResponseEntity<Object> response = postSchedule(schedule);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> postSchedule(Schedule schedule) {
        return restTestTemplate.postForEntity(API_1_0_SCHEDULES, schedule, Object.class);
    }


}
