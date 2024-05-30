package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SaleControllerTest {

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

    @Test
    void postSale_whenSaleIsValid_receiveOK() {
        Item item = TestUtil.createValidItem();
        itemRepository.save(item);
        Sale sale = Sale.builder().saleDate(new Date()).number(3).item(item).build();
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/api/1.0/sales", sale, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postSale_whenSaleIsValid_saleSaveToDatabase() {
        Item item = TestUtil.createValidItem();
        itemRepository.save(item);
        Sale sale = Sale.builder().saleDate(new Date()).number(3).item(item).build();
        testRestTemplate.postForEntity("/api/1.0/sales", sale, Object.class);
        assertThat(salerepository.count()).isEqualTo(1);
    }

}
