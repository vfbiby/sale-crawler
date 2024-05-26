package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemDTO;
import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.user.User;

import java.util.List;

public class TestUtil {

    static User createValidUser() {
        User user = new User();
        user.setDisplayName("test-display");
        user.setUsername("test-username");
        user.setPassword("P4sword");
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
        return Item.builder().itemId("32838242344").title("2024气质新款连衣裙")
                .pic("https://x.taobao.com/v.img").build();
    }
}
