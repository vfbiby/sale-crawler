package com.muhuang.salecrawler.rate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Data
public class NotesRateExistException extends RuntimeException {
    private final String message;

    public NotesRateExistException(String message) {
        this.message = message;
    }
}
