package com.muhuang.salecrawler.item.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.shop.Shop;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    @Column(length = 30,unique = true)
    private String outItemId;

    @NotNull
    @Size(min = 10, max = 60)
    @Column(length = 60)
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    private String pic;

    @ManyToOne
    @JoinColumn(name = "out_cate_id", referencedColumnName = "outCateId")
    private Cate cate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "out_shop_id", referencedColumnName = "outShopId")
    private Shop shop;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "out_item_id")
    @JsonIgnoreProperties({"item"})
    private List<Sale> saleList;

}

