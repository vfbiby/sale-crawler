package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.ExistsInDatabase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
    @ExistsInDatabase
    private String shopId;

    private String shopName;

    private String shopUrl;

    @Valid
    @NotEmpty(message = "{saleCrawler.constraints.items.empty.message}")
    private List<ItemDTO> items;

}
