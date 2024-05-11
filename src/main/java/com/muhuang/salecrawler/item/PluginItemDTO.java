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

    @NotNull
    private String shopId;

    private String shopName;

    private String shopUrl;

    private List<Item> item;

}
