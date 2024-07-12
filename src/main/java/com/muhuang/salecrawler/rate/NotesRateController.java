package com.muhuang.salecrawler.rate;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shop.ShopController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/1.0/NotesRate")
@CrossOrigin
public class NotesRateController {

    @Resource
    private NotesRateService notesRateService;

    @PostMapping
    NotesRate createNotesRate(@Valid @RequestBody NotesRate notesRate) {
        return notesRateService.save(notesRate);
    }

    @ExceptionHandler({NotesRateExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(NotesRateExistException exception, HttpServletRequest request) {
        HashMap<String, String> validationErrors = new HashMap<>();
        validationErrors.put("NotesRate", exception.getMessage());
        return new ApiError(400, "validation error", request.getServletPath(), validationErrors);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return ShopController.getApiError(exception, request);
    }

}
