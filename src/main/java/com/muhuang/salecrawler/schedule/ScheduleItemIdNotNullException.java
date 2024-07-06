package com.muhuang.salecrawler.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScheduleItemIdNotNullException extends RuntimeException {
}
