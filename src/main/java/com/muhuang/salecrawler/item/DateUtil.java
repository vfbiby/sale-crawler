package com.muhuang.salecrawler.item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class DateUtil {

    public static Date getFormatedYesterday() {
        Date yesterday = Date.from(new Date().toInstant().minus(Duration.ofDays(1)));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(yesterday);

        Date formatedYesterday = null;
        try {
            formatedYesterday = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return formatedYesterday;
    }

}
