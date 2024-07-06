package com.muhuang.salecrawler.sale;

import com.muhuang.salecrawler.item.entity.Item;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Sale findByItemAndSaleDate(@NotNull Item item, LocalDateTime saleDate);
}
