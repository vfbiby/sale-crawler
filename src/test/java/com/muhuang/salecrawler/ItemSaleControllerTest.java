package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemSaleControllerTest {

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private ItemRepository itemRepository;

    @Resource
    private SaleRepository saleRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @AfterEach
    public void cleanup() {
        saleRepository.deleteAll();
        itemRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Test
    void getItems_whenItemHasNoSale_receiveEmptySale() {
        Shop validShop = ShopControllerTest.createValidShop();
        shopRepository.save(validShop);
        Item item = TestUtil.createValidItem();
        item.setShop(validShop);
        itemRepository.save(item);
        ResponseEntity<TestPage<Item>> response = testRestTemplate.exchange("/api/1.0/items",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
        assertThat(items.get(0).getSaleList().isEmpty()).isTrue();
    }

    @Test
    void getItems_whenItemHasADaySale_receiveSaleListSizeIsOne() {
        Shop validShop = ShopControllerTest.createValidShop();
        shopRepository.save(validShop);
        Item item = TestUtil.createValidItem();
        item.setShop(validShop);
        Sale sale = new Sale();
        sale.setSaleDate(new Date());
        sale.setNumber(8);
        Sale save = saleRepository.save(sale);
        item.setSaleList(List.of(save));
        itemRepository.save(item);
        ResponseEntity<TestPage<Item>> response = testRestTemplate.exchange("/api/1.0/items",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
        assertThat(items.get(0).getSaleList().size()).isEqualTo(1);
    }

}
