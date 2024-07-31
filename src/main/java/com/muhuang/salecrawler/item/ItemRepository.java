package com.muhuang.salecrawler.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByOutItemId(String itemId);

    @Query(value = "select out_item_id from item", nativeQuery = true)
    List<String> getOutItemIds();
}
