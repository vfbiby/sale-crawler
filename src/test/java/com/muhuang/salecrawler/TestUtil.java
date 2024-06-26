package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemDTO;
import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.user.User;
import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TestUtil {

    static User createValidUser() {
        User user = new User();
        user.setDisplayName("test-display");
        user.setUsername("test-username");
        user.setPassword("P4sword");
        user.setImage("profile-image.png");
        return user;
    }

    static PluginItemDTO createValidShop() {
        return PluginItemDTO.builder().shopId("3423343434")
                .shopName("TT坏坏")
                .shopUrl("https://shop105703949.taobao.com").build();
    }

    static PluginItemDTO createValidPluginItem() {
        PluginItemDTO pItem = createValidShop();
        ItemDTO item = createItemDTO();
        pItem.setItems(List.of(item));
        return pItem;
    }

    public static ItemDTO createItemDTO() {
        return ItemDTO.builder().itemId("779612411768")
                .name("TT坏坏针织无袖长裙搭配吊带裙两件套女休闲度假风宽松设计感套装")
                .pic("https://img.taobao.com/main.jpg").build();
    }

    static Item createValidItem() {
        return createItemWithDetail("32838242344",
                LocalDate.now().toString(), "2024气质新款夏装连衣裙", "https://xx.taobao.com/v.img");
    }

    @SneakyThrows
    static Item createItemWithDetail(String itemId, String date, String title, String pic) {
        return Item.builder().itemId(itemId).title(title)
                .pic(pic).publishedAt(new SimpleDateFormat("yyyy-MM-dd").parse(date)).build();
    }
}
