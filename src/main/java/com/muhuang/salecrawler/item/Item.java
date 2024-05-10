package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.Shop;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 10,max = 30)
    @Column(length = 30)
    private String itemId;

    @NotNull
    @Size(min = 10, max = 60)
    @Column(length = 60)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "outShopId")
    private Shop shop;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", publishedAt=" + publishedAt +
                ", shop=" + shop +
                '}';
    }
}

