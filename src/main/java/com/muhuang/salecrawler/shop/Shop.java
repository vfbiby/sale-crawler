package com.muhuang.salecrawler.shop;

import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends BaseEntity {

    @NotNull
    @Column(unique = true)
    @UniqueShopId
    private String outShopId;

    @NotNull
    private String shopName;

    @URL
    @NotNull
    private String shopUrl;

}
