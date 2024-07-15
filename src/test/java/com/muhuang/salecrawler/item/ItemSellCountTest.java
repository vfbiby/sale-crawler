package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.sale.SaleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemSellCountTest {

    @Nested
    class SingleRealTimeFetch {

        @Resource
        ItemService itemService;

        @MockBean
        OneBoundService oneBoundService;


        @Test
        public void toFetchItemId_callSellCountApi_receiveTotalSellCount() {
            String toFetchItemId = "811528885164";
            Mockito.when(oneBoundService.getTaobaoDetail(toFetchItemId)).thenReturn(43);
            Integer totalSellCount = itemService.getTotalSellCount(toFetchItemId);
            assertThat(totalSellCount).isEqualTo(43);
        }

        @Test
        public void toFetchItemId_callSellCountApi_saveTotalSellCountToDB() {
            String toFetchItemId = "811528885164";
            Mockito.when(oneBoundService.getTaobaoDetail(toFetchItemId)).thenReturn(43);
            Integer totalSellCount = itemService.getTotalSellCount(toFetchItemId);
            itemService.saveSellCount(totalSellCount);
            Integer getTotalSellCount = itemService.getSellCount(totalSellCount);

            assertThat(getTotalSellCount).isEqualTo(43);
        }




    }


}
