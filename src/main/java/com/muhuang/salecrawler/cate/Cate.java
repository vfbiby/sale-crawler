package com.muhuang.salecrawler.cate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muhuang.salecrawler.shop.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"shop"})
public class Cate {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer outCateId;

    @ManyToOne
    @JoinColumn(name = "out_shop_id", referencedColumnName = "outShopId")
    private Shop shop;

    private String cateName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Cate Parent;
}
