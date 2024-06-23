package com.muhuang.salecrawler.item;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueItemValidator implements ConstraintValidator<UniqueItem, String> {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return itemRepository.findByOutItemId(value) == null;
    }
}
