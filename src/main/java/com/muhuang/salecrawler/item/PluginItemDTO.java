package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.ExistsInDatabase;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginItemDTO {

    private Long id;

    @NotNull(message = "{saleCrawler.constraints.shopId.NotNull.message}")
//    @ExistsInDatabase
    private String shopId;

    private String shopName;

    private String shopUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate PublishedAt;

    private Integer catId;

    private String catName;

    private Integer parentCatId;

    private String parentCatName;

    @Valid
    @NotEmpty(message = "{saleCrawler.constraints.items.empty.message}")
    private List<ItemDTO> items;

}
