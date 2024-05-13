package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    private Long id;

    private String itemId;

    private String name;

    private Shop shop;

}
