package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SaleControllerTest {

    public static final String API_1_0_SALES = "/api/1.0/sales";

    @Resource
    private TestRestTemplate testRestTemplate;

    @Resource
    private SaleRepository salerepository;

    @Resource
    private ItemRepository itemRepository;

    @AfterEach
    public void cleanup() {
        salerepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Nested
    class Create {

        @Test
        void postSale_whenSaleIsValid_receiveOK() {
            Item item = TestUtil.createValidItem();
            itemRepository.save(item);
            Sale sale = Sale.builder().saleDate(new Date()).number(3).item(item).build();
            ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_SALES, sale, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postSale_whenSaleIsValid_saleSaveToDatabase() {
            Item item = TestUtil.createValidItem();
            itemRepository.save(item);
            Sale sale = Sale.builder().saleDate(new Date()).number(3).item(item).build();
            testRestTemplate.postForEntity(API_1_0_SALES, sale, Object.class);
            assertThat(salerepository.count()).isEqualTo(1);
        }

        @Test
        void postSale_whenSaleHasNoItem_receiveBadRequest() {
            Sale sale = Sale.builder().saleDate(new Date()).number(3).build();
            ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_SALES, sale, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @Nested
    class Read {

        @Test
        void getSales_whenDatabaseHasNoSales_receiveOK() {
            ResponseEntity<Object> response = testRestTemplate.getForEntity(API_1_0_SALES, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void getSales_whenThereAreNoSalesInDB_receivePageWithZeroSales() {
            ResponseEntity<TestPage<Sale>> response = testRestTemplate.exchange(API_1_0_SALES,
                    HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
            assertThat(response.getBody().getTotalElements()).isEqualTo(0);
        }

        @Test
        void getSales_whenThereAreASalesInDB_receivePageWithOneSales() {
            Sale sale = Sale.builder().saleDate(new Date()).number(3).build();
            salerepository.save(sale);
            ResponseEntity<TestPage<Sale>> response = testRestTemplate.exchange(API_1_0_SALES,
                    HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
            assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        }

    }
}
