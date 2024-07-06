package com.muhuang.salecrawler.item.dto;

import com.muhuang.salecrawler.item.UniqueItem;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    private Long id;

    @NotNull
    @UniqueItem
    private String itemId;

    @NotNull
    private String name;

    @NotNull
    @URL
    private String pic;

}
