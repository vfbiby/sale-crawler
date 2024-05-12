package com.muhuang.salecrawler.item;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginItemDTO {

    private Long id;

    @NotNull(message = "{saleCrawler.constraints.shopId.NotNull.message}")
    private String shopId;

    private String shopName;

    private String shopUrl;

    @NotNull(message = "{saleCrawler.constraints.items.NotNull.message}")
    private List<Item> items;

}
