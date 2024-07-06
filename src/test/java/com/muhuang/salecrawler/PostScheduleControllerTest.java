package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.schedule.Schedule;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.muhuang.salecrawler.TestUtil.createValidShop;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostScheduleControllerTest {

    @Resource
    private ShopService shopRepository;
    @Resource
    private ItemService itemService;
    @Autowired
    private TestRestTemplate restTestTemplate;

    @Test
    void postSchedule_whenItemIsValid_receiveOK() {
        Item InDB = itemService.save(createValidItemWithShop());
        Schedule schedule = new Schedule();
        schedule.setOutItemId(InDB.getOutItemId());
        ResponseEntity<Object> response = restTestTemplate.postForEntity("/api/1.0/schedules", schedule, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public Item createValidItemWithShop() {
        Shop validShop = createValidShop();
        shopRepository.save(validShop);
        Item item = TestUtil.createValidItem();
        item.setShop(validShop);
        return item;
    }

}
