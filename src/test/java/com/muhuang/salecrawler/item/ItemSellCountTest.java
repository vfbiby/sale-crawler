package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemSellCountTest {

    final String mockJson = TestUtil.readJsonFromResources("taobao_item_get_app_response.json");
    final String itemId = "32838242344";

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


}
