package com.muhuang.salecrawler.shop;

import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsInDatabaseValidator implements ConstraintValidator<ExistsInDatabase, String> {

    @Resource
    private ShopRepository shopRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Shop byOutShopId = shopRepository.findByOutShopId(value);
        return byOutShopId != null;
    }
}
