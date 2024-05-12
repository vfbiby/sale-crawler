package com.muhuang.salecrawler.shop;

import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueShopIdValidator implements ConstraintValidator<UniqueShopId, String> {

    @Resource
    private ShopRepository shopRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Shop inDB = shopRepository.findByOutShopId(value);
        return inDB == null;
    }
}
