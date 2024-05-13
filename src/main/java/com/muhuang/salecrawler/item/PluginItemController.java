package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shared.GenericResponse;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopIsNotExistInDbException;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.muhuang.salecrawler.shop.ShopController.getApiError;

@RestController
@RequestMapping("/api/1.0/plugin-items")
public class PluginItemController {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;

    public PluginItemController(ItemRepository itemRepository,
                                ShopRepository shopRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
    }

    @PostMapping
    GenericResponse createPluginItems(@Valid @RequestBody PluginItemDTO pluginItemDTO) throws ShopIsNotExistInDbException {
        Shop byOutShopId = shopRepository.findByOutShopId(pluginItemDTO.getShopId());
        if (byOutShopId == null) {
            throw new ShopIsNotExistInDbException("Shop is not exist in db");
        }
        Stream<Item> itemStream = pluginItemDTO.getItems().stream().peek(item -> item.setShop(byOutShopId));
        List<Item> collect = itemStream.collect(Collectors.toList());
        itemRepository.saveAll(collect);
        return new GenericResponse("Plugin item saved!");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return getApiError(exception, request);
    }

}
