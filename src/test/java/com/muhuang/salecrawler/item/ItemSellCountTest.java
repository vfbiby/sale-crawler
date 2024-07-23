package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import com.muhuang.salecrawler.schedule.*;
import com.muhuang.salecrawler.share.TestUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemSellCountTest {

    private final String mockJson = TestUtil.readJsonFromResources("taobao_item_get_app_response.json");
    private final String itemId = "32838242344";

    @MockBean
    OneBoundService oneBoundService;

    @Resource
    ItemRepository itemRepository;

    @Nested
    class SingleRealTimeFetch {

        private final Date now = new Date();

        @Resource
        ItemService itemService;

        @Resource
        SaleRepository saleRepository;

        @BeforeEach
        public void setup() {
            itemRepository.deleteAll();
            saleRepository.deleteAll();
            Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
            itemRepository.save(TestUtil.createValidItem());
        }

        @Test
        public void fetchItemDetail_callSellCountApi_receiveItemDetailJson() {
            assertThat(oneBoundService.getTaobaoDetail(itemId)).isNotNull();
        }

        @Test
        public void fetchItemDetail_callSellCountApiAndParseJsonToGetSale_receiveSellCount() {
            Integer totalSellCount = itemService.getTotalSellCountByOneBound(itemId);
            assertThat(totalSellCount).isEqualTo(16);
        }

        @Test
        public void fetchItemDetail_callSellCountApiAndParseJsonToGetSale_sellCountSaveToDatabase() {
            Integer totalSellCount = itemService.getTotalSellCountByOneBound(itemId);
            itemService.saveSellCount(totalSellCount, itemId, now);
            assertThat(saleRepository.findAll().size()).isEqualTo(1);
        }

        @Test
        public void saveSellCount_ItemIsValid_sellCountAssociatedWithItem() {
            Integer sellCount = itemService.getTotalSellCountByOneBound(itemId);
            Sale sale = itemService.saveSellCount(sellCount, itemId);
            assertThat(sale.getItem().getOutItemId()).isEqualTo(itemId);
        }

        @Test
        public void saveSellCount_ItemIsValid_sellCountDateTypeIsJavaUtilDate() {
            Sale sale = itemService.saveSellCount(1, itemId);
            assertThat(sale.getSaleDate().getClass()).isEqualTo(Date.class);
        }

        @Test
        public void saveSellCount_ItemIsValid_sellCountDateIsYesterday() {
            Date formatedYesterday = DateUtil.getFormatedYesterday();
            Integer sellCount = itemService.getTotalSellCountByOneBound(itemId);
            Sale sale = itemService.saveSellCount(sellCount, itemId);
            assertThat(sale.getSaleDate()).isEqualTo(formatedYesterday);
        }

        @Test
        public void saveSellCount_ItemIsValidAndSellDateIsFirstDay_saveIncrementalSellCountIsEqualSellCount() {
            Integer totalSellCount = itemService.getTotalSellCountByOneBound(itemId);
            Sale sale = itemService.saveSellCount(totalSellCount, itemId);

            assertThat(sale.getIncrementalSellCount()).isEqualTo(totalSellCount);
        }

        @Test
        public void saveSellCount_ItemIsValid_saveIncrementalSellCountToDB() {
            Date dayBeforeYesterday = Date.from(now.toInstant().minus(Duration.ofDays(2)));
            itemService.saveSellCount(10, itemId, dayBeforeYesterday);

            Integer totalSellCount = itemService.getTotalSellCountByOneBound(itemId);
            Sale sale = itemService.saveSellCount(totalSellCount, itemId);

            assertThat(sale.getIncrementalSellCount()).isEqualTo(6);
        }

        @Nested
        class SadPath {
            @Test
            public void saveSellCount_ItemIsNull_throwSaleAssociatedItemMustNotBeNullException() {
                itemRepository.deleteAll();
                assertThatThrownBy(() -> itemService.saveSellCount(1, itemId))
                        .isInstanceOf(SaleAssociatedItemMustNotBeNullException.class);
            }

            @Test
            public void saveSellCount_throwSaleAssociatedItemMustNotBeNullException_hasItemIdMessage() {
                itemRepository.deleteAll();
                assertThatThrownBy(() -> itemService.saveSellCount(1, itemId))
                        .hasMessage("找不到 itemId=32838242344 的商品！");
            }

            @Test
            public void saveItem_ItemIdIsDuplicate_throwDataIntegrityViolationException() {
                assertThatThrownBy(() -> {
                    itemRepository.save(TestUtil.createValidItem());
                    itemRepository.save(TestUtil.createValidItem());
                }).isInstanceOf(DataIntegrityViolationException.class);
            }
        }

    }


    @Nested
    class QueueScheduleFetch {

        @Resource
        ScheduleRepository scheduleRepository;

        @Resource
        ScheduleService scheduleService;

        @BeforeEach
        void cleanup() {
            scheduleRepository.deleteAll();
        }

        @Nested
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
        class SaveToBeCrawledItemsToSchedule {

            @Test
            public void toCrawledItemIds_callSaveToBeCrawledItems_receiveNewRecords() {
                List<String> itemIds = List.of("111", "222");
                scheduleService.saveToBeCrawledItems(itemIds);

                assertThat(scheduleRepository.count()).isEqualTo(itemIds.size());
            }

            @Test
            public void toCrawledItemIds_callSaveToBeCrawledItemsAndParamsIsNull_receiveEmptyRecords() {
                scheduleService.saveToBeCrawledItems(null);

                assertThat(scheduleRepository.count()).isEqualTo(0);
            }

            @Test
            public void toCrawledItemIds_callSaveToBeCrawledItemsAndParamsIsEmpty_receiveEmptyRecords() {
                scheduleService.saveToBeCrawledItems(List.of());

                assertThat(scheduleRepository.count()).isEqualTo(0);
            }

            @Test
            public void toCrawledItemIds_callSaveToBeCrawledItemsAndItemIdIsPending_notSaveRecord() {
                scheduleRepository.save(Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
                scheduleService.saveToBeCrawledItems(List.of("111"));

                assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.RUNNING);
            }

        }

        @Nested
        class GetToBeCrawledItemIdsFromSchedule {

            @Test
            public void scheduleTable_callGetToBeCrawledItemIdsAndAllRecordsAreReadyStatus_receiveToCrawledItemIds() {
                scheduleRepository.save(Schedule.builder().outItemId("111").status(ScheduleStatus.READY).build());
                scheduleRepository.save(Schedule.builder().outItemId("222").status(ScheduleStatus.READY).build());
                List<String> toCrawledItemIds = scheduleService.getToBeCrawledItemIds();

                assertThat(toCrawledItemIds).isEqualTo(List.of("111", "222"));
            }

            @Test
            public void scheduleTable_callGetToBeCrawledItemIdsAndAllRecordsArePendingStatus_receiveEmptyList() {
                scheduleRepository.save(Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
                scheduleRepository.save(Schedule.builder().outItemId("222").status(ScheduleStatus.RUNNING).build());
                List<String> toCrawledItemIds = scheduleService.getToBeCrawledItemIds();

                assertThat(toCrawledItemIds.isEmpty()).isTrue();
            }

            @Test
            public void scheduleTable_callGetToBeCrawledItemIdsAndRecordsHasAllStatus_receiveToCrawledItemIds() {
                scheduleRepository.save(Schedule.builder().outItemId("111").status(ScheduleStatus.RUNNING).build());
                scheduleRepository.save(Schedule.builder().outItemId("222").status(ScheduleStatus.READY).build());
                List<String> toCrawledItemIds = scheduleService.getToBeCrawledItemIds();

                assertThat(toCrawledItemIds).isEqualTo(List.of("222"));
            }

            @Test
            public void scheduleTable_callGetToBeCrawledItemIdsAndHasNoneRecords_receiveEmptyList() {
                List<String> toCrawledItemIds = scheduleService.getToBeCrawledItemIds();

                assertThat(toCrawledItemIds.isEmpty()).isTrue();
            }
        }

        @Nested
        class CallSaveSellCount {

            @BeforeEach
            void cleanup() {
                itemRepository.deleteAll();
            }

            @Test
            public void scheduleTable_callSaveSellCount_receiveOk() {
                Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.READY).build());

                String toCrawledItemId = scheduleService.getToBeCrawledItemIds().get(0);
                Boolean result = scheduleService.saveSellCount(toCrawledItemId);

                assertThat(result).isTrue();
            }

            @Test
            public void scheduleTable_callSaveSellCountAndSuccessful_deleteSuccessfulItemIdFromSchedule() {
                Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.READY).build());

                String toCrawledItemId = scheduleService.getToBeCrawledItemIds().get(0);
                scheduleService.saveSellCount(toCrawledItemId);

                assertThat(scheduleRepository.count()).isEqualTo(0);
            }

            @Test
            public void scheduleTable_callSaveSellCountAndFailed_itemInScheduleIsReadyStatus() {
                Mockito.when(oneBoundService.getTaobaoDetail("333")).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.READY).build());

                String toCrawledItemId = scheduleService.getToBeCrawledItemIds().get(0);
                try {
                    scheduleService.saveSellCount(toCrawledItemId);
                } catch (Exception ignored) {
                }

                assertThat(scheduleRepository.findAll().get(0).getStatus()).isEqualTo(ScheduleStatus.READY);
            }

            @Test
            public void scheduleTable_callSaveSellCountAndFailed_throwItemCrawlFailedException() {
                Mockito.when(oneBoundService.getTaobaoDetail("333")).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.READY).build());

                String toCrawledItemId = scheduleService.getToBeCrawledItemIds().get(0);

                assertThatThrownBy(() -> scheduleService.saveSellCount(toCrawledItemId))
                        .isInstanceOf(ItemCrawlFailedException.class);
            }

            @Test
            public void scheduleTable_callSaveSellCountAndFailed_throwItemCrawlFailedExceptionHasCorrectMessage() {
                Mockito.when(oneBoundService.getTaobaoDetail("333")).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.READY).build());

                String toCrawledItemId = scheduleService.getToBeCrawledItemIds().get(0);

                assertThatThrownBy(() -> scheduleService.saveSellCount(toCrawledItemId))
                        .hasMessage(String.format("itemId=%s的商品，调用销量数据接口失败！", itemId));
            }

            @Test
            public void scheduleTable_callSaveSellCountAndItemIsRunning_throwItemIsCrawlingException() {
                Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.RUNNING).build());

                assertThatThrownBy(() -> scheduleService.saveSellCount(itemId))
                        .isInstanceOf(ItemIsCrawlingException.class);
            }

            @Test
            public void scheduleTable_callSaveSellCountAndItemIsRunning_throwItemIsCrawlingExceptionHasCorrectMessage() {
                Mockito.when(oneBoundService.getTaobaoDetail(itemId)).thenReturn(mockJson);
                itemRepository.save(TestUtil.createValidItem());
                scheduleRepository.save(Schedule.builder().outItemId(itemId).status(ScheduleStatus.RUNNING).build());

                assertThatThrownBy(() -> scheduleService.saveSellCount(itemId))
                        .hasMessage(String.format("itemId=%s的商品，正在爬取并存储销量信息！", itemId));
            }

        }

    }


}
