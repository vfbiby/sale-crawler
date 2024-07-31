package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.share.TestPage;
import com.muhuang.salecrawler.share.TestUtil;
import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

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

    @Resource
    private CateRepository cateRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void cleanup() {
        cateRepository.deleteAll();
        saleRepository.deleteAll();
        itemRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Test
    void getItems_whenItemHasNoSale_receiveEmptySale() {
        Item item = TestUtil.createValidItem();
        itemRepository.save(item);
        ResponseEntity<TestPage<Item>> response = postItem(new ParameterizedTypeReference<>() {
        });
        List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
        assertThat(items.get(0).getSaleList().isEmpty()).isTrue();
    }

    @Test
    void getItems_whenItemHasOneDaySale_receiveSaleListSizeIsOne() {
        Item item = TestUtil.createValidItem();
        item.setSaleList(List.of(createSale()));
        itemRepository.save(item);
        ResponseEntity<TestPage<Item>> response = postItem(new ParameterizedTypeReference<>() {
        });
        List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
        assertThat(items.get(0).getSaleList().size()).isEqualTo(1);
    }

    private ResponseEntity<TestPage<Item>> postItem(ParameterizedTypeReference<TestPage<Item>> responseType) {
        return testRestTemplate.exchange("/api/1.0/items",
                HttpMethod.GET, null, responseType);
    }

    private static Sale createSale() {
        Sale sale = new Sale();
        sale.setSaleDate(new Date());
        sale.setSellCount(8);
        return sale;
    }

}
