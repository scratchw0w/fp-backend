package com.fstore.model.dto;

import com.fstore.model.ProductType;

import java.math.BigDecimal;

public record ProductResponseDto(
        int id,
        String title,
        String description,
        BigDecimal price,
        ProductType type,
        ProductImageDto image) {
}
