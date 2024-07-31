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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
// 只关注 拿到 item ， 和 status 的变换。
public class ScheduleTest {

    private final ItemSellCountTest itemSellCountTest;
    @Resource
    ScheduleRepository scheduleRepository;

    @Resource
    ScheduleService scheduleService;

    public ScheduleTest(ItemSellCountTest itemSellCountTest) {
        this.itemSellCountTest = itemSellCountTest;
    }

    @BeforeEach
    void cleanup() {
        scheduleRepository.deleteAll();
    }

    @Nested
    @Disabled(value = "没必要")
    class GetToBeCrawledItemIdsFromItem {

        @Resource
        ItemRepository itemRepository;

        @Resource
        ItemService itemService;

        @BeforeEach
        void cleanup() {
            itemRepository.deleteAll();
        }

        @Test
        public void itemTable_callGetToBeCrawledItemIdsAndItemTableHasRecord_receiveItemWithId() {
            Item item1 = TestUtil.createValidItem();
            item1.setOutItemId("111");
            Item item2 = TestUtil.createValidItem();
            item2.setOutItemId("222");
            itemRepository.saveAll(List.of(item1, item2));

            List<String> itemIds = itemService.getToBeCrawledItemIds();
            assertThat(itemIds).isEqualTo(List.of("111", "222"));
        }

        @Test
        public void itemTable_callGetToBeCrawledItemIdsAndItemTableIsEmpty_receiveEmptyList() {
            List<String> itemIds = itemService.getToBeCrawledItemIds();
            assertThat(itemIds.isEmpty()).isTrue();
        }
    }

    @Nested
    class SavePendingItemsToSchedule {

        @Test
        public void getPendingItems_whenSaveToBeCrawledItems_saveItemIdsToDatabase() {
            List<String> itemIds = List.of("111", "222");
            scheduleService.savePendingItemIds(itemIds);

            assertThat(scheduleRepository.count()).isEqualTo(itemIds.size());
        }

        // assert pending status


        @Test
        public void toCrawledItemIds_callSaveToBeCrawledItemsAndParamsIsNull_receiveEmptyRecords() {
            scheduleService.savePendingItemIds(null);

            assertThat(scheduleRepository.count()).isEqualTo(0);
        }

        @Test
        public void toCrawledItemIds_callSaveToBeCrawledItemsAndParamsIsEmpty_receiveEmptyRecords() {
            scheduleService.savePendingItemIds(List.of());

            assertThat(scheduleRepository.count()).isEqualTo(0);
        }

        @Test
        public void toCrawledItemIds_callSaveToBeCrawledItemsAndItemIdIsRunning_notSaveRecord() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
            scheduleService.savePendingItemIds(List.of("111"));

            assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.RUNNING);
        }

    }

    @Nested
    class GetPendingItems {

        @Test
        public void _thereIsOnePendingItemInSchedule_receiveOne() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.PENDING).build());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("222").status(ScheduleStatus.PENDING).build());
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds).isEqualTo(List.of("111", "222"));
        }

        @Test
        public void getPendingItem_thereIsNoPendingItemInSchedule_receiveEmpty() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("222").status(ScheduleStatus.RUNNING).build());
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds.isEmpty()).isTrue();
        }

        @Test
        public void scheduleTable_callGetToBeCrawledItemIdsAndRecordsHasAllStatus_receiveToCrawledItemIds() {
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId("222").status(ScheduleStatus.PENDING).build());
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds).isEqualTo(List.of("222"));
        }

        @Test
        public void scheduleTable_callGetToBeCrawledItemIdsAndHasNoneRecords_receiveEmptyList() {
            List<String> toCrawledItemIds = scheduleService.getPendingItemIds();

            assertThat(toCrawledItemIds.isEmpty()).isTrue();
        }
    }

    @Nested
    class ChangeStatus {


    }

    @Nested
    class CallSaveSellCount {

        @BeforeEach
        void cleanup() {
            itemSellCountTest.itemRepository.deleteAll();
        }

        @Test
        public void scheduleTable_callSaveSellCount_receiveOk() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail(itemSellCountTest.itemId)).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);
            Boolean result = scheduleService.saveSellCount(toCrawledItemId);

            assertThat(result).isTrue();
        }

        @Test
        public void scheduleTable_callSaveSellCountAndSuccessful_deleteSuccessfulItemIdFromSchedule() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail(itemSellCountTest.itemId)).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);
            scheduleService.saveSellCount(toCrawledItemId);

            assertThat(scheduleRepository.count()).isEqualTo(0);
        }

        @Test
        public void scheduleTable_callSaveSellCountAndFailed_itemInScheduleIsReadyStatus() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail("333")).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);
            try {
                scheduleService.saveSellCount(toCrawledItemId);
            } catch (Exception ignored) {
            }

            assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.PENDING);
        }

        @Test
        public void scheduleTable_callSaveSellCountAndFailed_throwItemCrawlFailedException() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail("333")).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);

            assertThatThrownBy(() -> scheduleService.saveSellCount(toCrawledItemId))
                    .isInstanceOf(ItemCrawlFailedException.class);
        }

        @Test
        public void scheduleTable_callSaveSellCountAndFailed_throwItemCrawlFailedExceptionHasCorrectMessage() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail("333")).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.PENDING).build());

            String toCrawledItemId = scheduleService.getPendingItemIds().get(0);

            assertThatThrownBy(() -> scheduleService.saveSellCount(toCrawledItemId))
                    .hasMessage(String.format("itemId=%s的商品，调用销量数据接口失败！", itemSellCountTest.itemId));
        }

        @Test
        public void scheduleTable_callSaveSellCountAndItemIsRunning_throwItemIsCrawlingException() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail(itemSellCountTest.itemId)).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.RUNNING).build());

            assertThatThrownBy(() -> scheduleService.saveSellCount(itemSellCountTest.itemId))
                    .isInstanceOf(ItemIsCrawlingException.class);
        }

        @Test
        public void scheduleTable_callSaveSellCountAndItemIsRunning_throwItemIsCrawlingExceptionHasCorrectMessage() {
            Mockito.when(itemSellCountTest.oneBoundService.getTaobaoDetail(itemSellCountTest.itemId)).thenReturn(itemSellCountTest.mockJson);
            itemSellCountTest.itemRepository.save(TestUtil.createValidItem());
            scheduleRepository.save(com.muhuang.salecrawler.schedule.Schedule.builder().outItemId(itemSellCountTest.itemId).status(ScheduleStatus.RUNNING).build());

            assertThatThrownBy(() -> scheduleService.saveSellCount(itemSellCountTest.itemId))
                    .hasMessage(String.format("itemId=%s的商品，正在爬取并存储销量信息！", itemSellCountTest.itemId));
        }

    }

}
