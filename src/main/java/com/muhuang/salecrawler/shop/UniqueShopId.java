package com.muhuang.salecrawler.shop;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueShopIdValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueShopId {

    String message() default "{saleCrawler.constraints.shop.uniqueOutShopId.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
