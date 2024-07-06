package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.schedule.Schedule;
import com.muhuang.salecrawler.schedule.ScheduleRepository;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.muhuang.salecrawler.TestUtil.createValidShop;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostScheduleTest {

    @Resource
    private ItemService itemService;
    @Resource
    private ShopRepository shopRepository;
    @Resource
    private TestRestTemplate restTestTemplate;
    @Resource
    private ScheduleRepository scheduleRepository;

    @Test
    void postSchedule_whenItemIsValid_receiveOK() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId("3423423");
        ResponseEntity<Object> response = restTestTemplate.postForEntity("/api/1.0/schedules", schedule, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postSchedule_whenItemIsValid_itemSaveToDatabase() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId("34234324");
        restTestTemplate.postForEntity("/api/1.0/schedules", schedule, Object.class);
        assertThat(scheduleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void postSchedule_whenScheduleHasNullItemId_receiveBadRequest() {
        Schedule schedule = new Schedule();
        schedule.setOutItemId(null);
        ResponseEntity<Object> response = restTestTemplate.postForEntity("/api/1.0/schedules", schedule, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    public Item createValidItemWithShop() {
        Shop validShop = createValidShop();
        shopRepository.save(validShop);
        Item item = TestUtil.createValidItem();
        item.setShop(validShop);
        return item;
    }

}
