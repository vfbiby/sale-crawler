package com.muhuang.salecrawler.shop;

import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final CateRepository cateRepository;
    private final ItemService itemService;

    private static Function<ItemDTO, Item> getItemDTOItemFunction(PluginItemDTO pluginItemDTO, Shop inDB, Cate savedCate) {
        return itemDTO -> {
            Item.ItemBuilder itemBuilder = buildItem(itemDTO, inDB);
            if (pluginItemDTO.getPublishedAt() != null) {
                itemBuilder.publishedAt(Date.from(pluginItemDTO.getPublishedAt()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            itemBuilder.cate(savedCate);
            return itemBuilder.build();
        };
    }

    private static Item.ItemBuilder buildItem(ItemDTO itemDTO, Shop inDB) {
        return Item.builder().outItemId(itemDTO.getItemId()).title(itemDTO.getName())
                .shop(inDB).pic(itemDTO.getPic());
    }

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    public Page<Shop> getShops() {
        PageRequest pageable = PageRequest.of(0, 10);
        return shopRepository.findAll(pageable);
    }

    public void saveShop(PluginItemDTO pluginItemDTO) {
        Shop shopInDB = getShop(pluginItemDTO);
        Cate savedCate = getCate(pluginItemDTO, shopInDB);
        saveAll(pluginItemDTO, shopInDB, savedCate);
    }

    private Shop getShop(PluginItemDTO pluginItemDTO) {
        Shop inDB = shopRepository.findByOutShopId(pluginItemDTO.getShopId());
        if (inDB == null) {
            Shop shop = Shop.builder().outShopId(pluginItemDTO.getShopId())
                    .shopName(pluginItemDTO.getShopName()).shopUrl(pluginItemDTO.getShopUrl()).build();
            return shopRepository.save(shop);
        }
        return inDB;
    }

    private Cate getCate(PluginItemDTO pluginItemDTO, Shop inDB) {
        Integer cateId = pluginItemDTO.getCatId();
        if (cateId == null) return null;
        String cateName = pluginItemDTO.getCatName();
        Cate cate = Cate.builder().outCateId(cateId).cateName(cateName).shop(inDB).build();
        if (pluginItemDTO.getParentCatId() != null) {
            Cate parentCate = Cate.builder().cateName(pluginItemDTO.getParentCatName())
                    .outCateId(pluginItemDTO.getParentCatId()).shop(inDB).build();
            Cate savedParentCate = cateRepository.save(parentCate);
            cate.setParent(savedParentCate);
        }
        Optional<Cate> byId = Optional.ofNullable(cateRepository.findByOutCateId(cateId));
        if (byId.isPresent() && byId.get().getParent() == null) {
            byId.get().setParent(cate.getParent());
            cateRepository.save(byId.get());
        }
        return byId.orElseGet(() -> cateRepository.save(cate));
    }

    private void saveAll(PluginItemDTO pluginItemDTO, Shop shopInDB, Cate savedCate) {
        Stream<Item> itemStream = pluginItemDTO.getItems().stream().map(getItemDTOItemFunction(pluginItemDTO, shopInDB, savedCate));
        List<Item> collect = itemStream.collect(Collectors.toList());
        itemService.saveAll(collect);
    }
}
