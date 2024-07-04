package com.muhuang.salecrawler.shop;

import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shared.GenericResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/1.0/shops")
public class ShopController {

    @Resource
    private ShopService shopService;

    @PostMapping
    public GenericResponse createShop(@Valid @RequestBody Shop shop) {
        shopService.save(shop);
        return new GenericResponse("user saved!");
    }

    @GetMapping
    Page<Shop> getShops() {
        return shopService.getShops();
    }

    @PostMapping("/plugin-items")
    GenericResponse createPluginItems(@Valid @RequestBody PluginItemDTO pluginItemDTO) {
        shopService.saveShop(pluginItemDTO);
        return new GenericResponse("Plugin item saved!");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return getApiError(exception, request);
    }

    public static ApiError getApiError(MethodArgumentNotValidException exception, HttpServletRequest request) {
        BindingResult bindingResult = exception.getBindingResult();

        HashMap<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ApiError(400, "validation error", request.getServletPath(), validationErrors);
    }
}
