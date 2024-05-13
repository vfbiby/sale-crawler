package com.muhuang.salecrawler.shop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ExistsInDatabaseValidator implements ConstraintValidator<ExistsInDatabase, String> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        List<Shop> existingShop = entityManager
                .createQuery("SELECT s FROM Shop s WHERE s.outShopId = :outShopId", Shop.class)
                .setParameter("outShopId", value)
                .getResultList();

        return existingShop.size() == 1;
    }
}
