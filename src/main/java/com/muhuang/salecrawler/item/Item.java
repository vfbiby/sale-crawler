package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.Shop;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 10, max = 30)
    @Column(length = 30)
    private String itemId;

    @NotNull
    @Size(min = 10, max = 60)
    @Column(length = 60)
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "out_shop_id", referencedColumnName = "outShopId")
    private Shop shop;

}

