package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.schedule.*;
import com.muhuang.salecrawler.share.TestUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ScheduleTableTest {


    @Resource
    ScheduleRepository scheduleRepository;

    @Resource
    ScheduleService scheduleService;


    @BeforeEach
    void cleanup() {
        scheduleRepository.deleteAll();
    }


    @Nested
    class SavePendingItemsToSchedule {

        @Test
        public void getPendingItems_whenSavePendingItemIds_saveItemsToDatabase() {
            List<String> itemIds = List.of("111", "222");
            scheduleService.savePendingItemIds(itemIds);

            assertThat(scheduleRepository.count()).isEqualTo(itemIds.size());
        }

        @Test
        public void getPendingItems_whenSavePendingItemIds_saveItemStatusToDatabase() {
            List<String> itemIds = List.of("111", "222");
            scheduleService.savePendingItemIds(itemIds);

            assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.PENDING);
        }


        @Test
        public void getPendingItems_whenParamsIsNull_notSaveItems() {
            scheduleService.savePendingItemIds(null);

            assertThat(scheduleRepository.count()).isEqualTo(0);
        }

        @Test
        public void getPendingItems_whenParamsIsEmpty_notSaveItems() {
            scheduleService.savePendingItemIds(List.of());

            assertThat(scheduleRepository.count()).isEqualTo(0);
        }

        @Test
        public void getPendingItems_whenItemIdIsRunning_notSaveItems() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
            scheduleService.savePendingItemIds(List.of("111"));

            assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.RUNNING);
        }

    }

    @Nested
    class GetPendingItems {

        @Test
        public void _thereIsTwoPendingItemsInSchedule_receiveTwo() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.PENDING).build());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("222").status(ScheduleStatus.PENDING).build());
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds).isEqualTo(List.of("111", "222"));
        }

        @Test
        public void _thereIsTwoRunningItemsInSchedule_receiveEmpty() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("222").status(ScheduleStatus.RUNNING).build());
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds.isEmpty()).isTrue();
        }

        @Test
        public void _thereIsOneRunningItemAndOnePendingItemInSchedule_receiveOne() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("222").status(ScheduleStatus.PENDING).build());
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds).isEqualTo(List.of("222"));
        }

        @Test
        public void _thereIsZeroItemInSchedule_receiveEmpty() {
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds.isEmpty()).isTrue();
        }
    }


}
