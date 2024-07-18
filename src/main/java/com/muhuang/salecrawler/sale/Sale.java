package com.muhuang.salecrawler.sale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"item"})
public class Sale extends BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;

    private int number;

    private int incrementalSellCount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "out_item_id")
    @JsonIgnoreProperties({"saleList"})
    private Item item;
}
