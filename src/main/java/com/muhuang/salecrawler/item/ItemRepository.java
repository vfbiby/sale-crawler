package com.muhuang.salecrawler.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * 根据 outItemId 查询商品
     *
     * @param outItemId 商品 id
     * @return 商品
     */
    Optional<Item> findByOutItemId(String outItemId);
}
