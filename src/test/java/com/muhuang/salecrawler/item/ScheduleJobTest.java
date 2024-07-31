package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.configuration.ScheduleConfiguration;
import com.muhuang.salecrawler.schedule.*;
import com.muhuang.salecrawler.share.TestUtil;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ScheduleJobTest {


    @Nested
    class SaveSellCount {

        final String mockJson = TestUtil.readJsonFromResources("taobao_item_get_app_response.json");
        final String itemId = "32838242344";

        @MockBean
        OneBoundService oneBoundService;

        @Resource
        ItemRepository itemRepository;

        @Resource
        ScheduleRepository scheduleRepository;

        @Resource
        ScheduleService scheduleService;

        @BeforeEach
        void cleanup() {
            itemRepository.deleteAll();
            scheduleRepository.deleteAll();
        }

        @Test
        public void _thereIsOnePendingInSchedule_receiveSuccessful() {
            Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);
            Boolean result = scheduleService.saveSellCount(toCrawledItemId);

            assertThat(result).isTrue();
        }

        @Test
        public void _thereIsOnePendingInSchedule_deleteItemInSchedule() {
            Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);
            scheduleService.saveSellCount(toCrawledItemId);

            Assertions.assertThat(scheduleRepository.count()).isEqualTo(0);
        }

        @Test
        public void _thereIsOnePendingInScheduleAndSaveFailed_itemIsPendingInSchedule() {
            Mockito.when(oneBoundService.getTaobaoDetail("333")).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);
            try {
                scheduleService.saveSellCount(toCrawledItemId);
            } catch (Exception ignored) {
            }

            Assertions.assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.PENDING);
        }

        @Test
        public void _thereIsOtherItemPendingInSchedule_throwItemCrawlFailedException() {
            Mockito.when(oneBoundService.getTaobaoDetail("333")).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);

            assertThatThrownBy(() -> scheduleService.saveSellCount(toCrawledItemId))
                    .isInstanceOf(ItemCrawlFailedException.class);
        }

        @Test
        public void _thereIsOtherItemPendingInSchedule_throwItemCrawlFailedExceptionHasCorrectMessage() {
            Mockito.when(oneBoundService.getTaobaoDetail("333")).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);

            assertThatThrownBy(() -> scheduleService.saveSellCount(toCrawledItemId))
                    .hasMessage(String.format("itemId=%s的商品，调用销量数据接口失败！", itemId));
        }

        @Test
        public void _thereIsOneRunningInSchedule_throwItemIsCrawlingException() {
            Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.RUNNING).build());

            assertThatThrownBy(() -> scheduleService.saveSellCount(itemId))
                    .isInstanceOf(ItemIsCrawlingException.class);
        }

        @Test
        public void _thereIsOneRunningInSchedule_throwItemIsCrawlingExceptionHasCorrectMessage() {
            Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemId).status(ScheduleStatus.RUNNING).build());

            assertThatThrownBy(() -> scheduleService.saveSellCount(itemId))
                    .hasMessage(String.format("itemId=%s的商品，正在爬取并存储销量信息！", itemId));
        }

    }


    @Nested
    @Import(ScheduleConfiguration.class)
    class ScheduleJob {

        @Resource
        ScheduleJobService scheduleJobService;

        @Test
        public void _spend2Second_callSaveSellCountLeastTwice() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            assertThat(scheduleJobService.getCount()).hasValueGreaterThanOrEqualTo(2);
        }

    }

}
