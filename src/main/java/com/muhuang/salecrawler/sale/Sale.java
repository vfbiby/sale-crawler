package com.muhuang.salecrawler.sale;

import com.muhuang.salecrawler.item.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;

    private int number;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
