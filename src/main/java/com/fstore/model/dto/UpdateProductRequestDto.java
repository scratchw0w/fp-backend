package com.fstore.model.dto;

import com.fstore.model.ProductType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequestDto(
        @NotEmpty(message = "Title cannot be empty")
        String title,
        String description,
        @DecimalMax(value = "99999.99", message = "Price is not valid")
        @DecimalMin(value = "0.0", message = "Price is not valid")
        BigDecimal price,
        ProductType type) {
}
