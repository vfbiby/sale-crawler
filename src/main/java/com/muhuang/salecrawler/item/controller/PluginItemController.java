package com.muhuang.salecrawler.item.controller;

import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.entity.Item;
import com.muhuang.salecrawler.item.dto.ItemDTO;
import com.muhuang.salecrawler.item.dto.PluginItemDTO;
import com.muhuang.salecrawler.item.service.ItemService;
import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shared.GenericResponse;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopController;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/1.0/plugin-items")
public class PluginItemController {

    private final ShopRepository shopRepository;
    private final ItemService itemService;
    private final CateRepository cateRepository;

    public PluginItemController(ShopRepository shopRepository, ItemService itemService,
                                CateRepository cateRepository) {
        this.shopRepository = shopRepository;
        this.itemService = itemService;
        this.cateRepository = cateRepository;
    }

    @PostMapping
    GenericResponse createPluginItems(@Valid @RequestBody PluginItemDTO pluginItemDTO) {
        Shop inDB = getShop(pluginItemDTO);
        Cate savedCate = getCate(pluginItemDTO, inDB);
        Stream<Item> itemStream = pluginItemDTO.getItems().stream().map(itemDTO -> {
            Item.ItemBuilder itemBuilder = buildItem(itemDTO, inDB);
            if (pluginItemDTO.getPublishedAt() != null) {
                itemBuilder.publishedAt(Date.from(pluginItemDTO.getPublishedAt()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            itemBuilder.cate(savedCate);
            return itemBuilder.build();
        });
        List<Item> collect = itemStream.collect(Collectors.toList());
        itemService.saveAll(collect);
        return new GenericResponse("Plugin item saved!");
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

    private static Item.ItemBuilder buildItem(ItemDTO itemDTO, Shop inDB) {
        return Item.builder().outItemId(itemDTO.getItemId()).title(itemDTO.getName())
                .shop(inDB).pic(itemDTO.getPic());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return ShopController.getApiError(exception, request);
    }

}
