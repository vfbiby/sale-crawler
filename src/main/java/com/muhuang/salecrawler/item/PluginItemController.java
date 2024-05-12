package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shared.GenericResponse;
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
        if (shopRepository.findByOutShopId(pluginItemDTO.getShopId()) == null) {
            throw new ShopIsNotExistInDbException("Shop is not exist in db");
        }
        itemRepository.saveAll(pluginItemDTO.getItems());
        return new GenericResponse("Plugin item saved!");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return getApiError(exception, request);
    }

}
