package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.sale.SaleRepository;
import com.muhuang.salecrawler.taobao.TaobaoHttpClient;
import com.muhuang.salecrawler.taobao.TaobaoSaleMonthlyResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Frank An
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:database/test-shop-data.sql",
        "classpath:database/test-cate-data.sql",
        "classpath:database/test-item-data.sql",
})
public class FavoriteItemControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private TaobaoHttpClient taobaoHttpClient;
    @Autowired
    private SaleRepository saleRepository;

//1. postFavoriteItem_whenItemHasSale_saveToSaleTable
//2. postFavoriteItem_whenItemDoesNotExists_receiveMessageOfItemDoesNotExists
//3. postFavoriteItem_whenItemIdIsNull_receiveMessageOfItemIdIsNull
//4. postFavoriteItem_whenItemIdIsNull_receiveBadRequest

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
    }

    @Test
    void postFavoriteItem_whenItemHasSale_saveToSaleTable() {
        when(taobaoHttpClient.getMonthlySaleNum(anyString(), anyString()))
                .thenReturn(new TaobaoSaleMonthlyResult(1, "", 1000, ""));
        testRestTemplate
                .postForEntity("/api/1.0/items/1/favorite", null, Object.class);
        assertThat(saleRepository.count()).isEqualTo(1);
    }
}
