package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shared.GenericResponse;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopIsNotExistInDbException;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.muhuang.salecrawler.shop.ShopController.getApiError;

@RestController
@RequestMapping("/api/1.0/plugin-items")
public class PluginItemController {

    private final ShopRepository shopRepository;
    private final ItemService itemService;

    public PluginItemController(ShopRepository shopRepository, ItemService itemService) {
        this.shopRepository = shopRepository;
        this.itemService = itemService;
    }

    @PostMapping
    GenericResponse createPluginItems(@Valid @RequestBody PluginItemDTO pluginItemDTO) throws ShopIsNotExistInDbException {
        Shop inDB = shopRepository.findByOutShopId(pluginItemDTO.getShopId());
        Stream<Item> itemStream = pluginItemDTO.getItems().stream().map(itemDTO ->
                Item.builder().itemId(itemDTO.getItemId()).title(itemDTO.getName())
                        .shop(inDB).pic(itemDTO.getPic()).build());
        List<Item> collect = itemStream.collect(Collectors.toList());
        itemService.saveAll(collect);
        return new GenericResponse("Plugin item saved!");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return getApiError(exception, request);
    }

}
