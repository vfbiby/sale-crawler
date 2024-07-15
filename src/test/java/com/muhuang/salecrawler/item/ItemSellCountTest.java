package com.muhuang.salecrawler.item;

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

        @MockBean
        ItemService itemService;

        @Test
        public void toFetchItemId_callSellCountApi_receiveTotalSellCount() {
            String toFetchItemId = "811528885164";
            Mockito.when(itemService.getTotalSellCount(toFetchItemId)).thenReturn(43);
            Integer totalSellCount = itemService.getTotalSellCount(toFetchItemId);
            assertThat(totalSellCount).isEqualTo(43);
        }


    }


}
