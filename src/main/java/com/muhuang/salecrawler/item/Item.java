package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String itemId;

    private String name;

    private String shopId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

}

