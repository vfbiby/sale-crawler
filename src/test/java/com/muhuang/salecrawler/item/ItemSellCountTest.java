package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import com.muhuang.salecrawler.share.TestUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemSellCountTest {

    @Nested
    class SingleRealTimeFetch {

        private final String toFetchItemId = "32838242344";
        private final Date now = new Date();
        String mockJson = TestUtil.readJsonFromResources("taobao_item_get_app_response.json");

        @Resource
        ItemService itemService;

        @MockBean
        OneBoundService oneBoundService;

        @Resource
        ItemRepository itemRepository;

        @Resource
        SaleRepository saleRepository;

        @BeforeEach
        public void setup() {
            itemRepository.save(TestUtil.createValidItem());
            Mockito.when(oneBoundService.getTaobaoDetail(toFetchItemId)).thenReturn(mockJson);
        }

        @AfterEach
        public void cleanup() {
            itemRepository.deleteAll();
            saleRepository.deleteAll();
        }

        @Test
        public void toFetchItemId_callSellCountApi_receiveTotalSellCount() {
            Integer totalSellCount = itemService.getTotalSellCountByOneBound(toFetchItemId);
            assertThat(totalSellCount).isEqualTo(16);
        }

        @Test
        public void toFetchItemId_callSellCountApi_saveTotalSellCountToDB() {
            Sale sale = getCurrentDaySale();
            assertThat(sale.getNumber()).isEqualTo(16);
        }

        @Test
        public void toFetchItemId_callSellCountApi_sellCountIsAssociatedItem() {
            Sale sale = getCurrentDaySale();
            assertThat(sale.getItem().getOutItemId()).isEqualTo(toFetchItemId);
        }

        @Test
        public void toFetchItemId_callSellCountApi_saveInterDaySellCountToDB() {
            Date yesterday = Date.from(now.toInstant().minus(Duration.ofDays(1)));
            itemService.saveSellCount(10, toFetchItemId, yesterday);
            Sale sale = getCurrentDaySale();

            assertThat(sale.getInterdaySellCount()).isEqualTo(6);
        }

        private Sale getCurrentDaySale() {
            Integer totalSellCount = itemService.getTotalSellCountByOneBound(toFetchItemId);
            itemService.saveSellCount(totalSellCount, toFetchItemId, now);
            return itemService.getSale(toFetchItemId, now);
        }
    }
}
