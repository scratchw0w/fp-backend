package com.fstore.model;

import lombok.With;

import java.math.BigDecimal;

@With
public record Product(
        int id,
        String title,
        String description,
        BigDecimal price,
        ProductType type,
        ProductImage image) {
}
