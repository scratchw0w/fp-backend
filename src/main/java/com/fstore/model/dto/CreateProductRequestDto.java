package com.fstore.model.dto;

import com.fstore.model.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequestDto(
        @NotEmpty(message = "Title cannot be empty")
        String title,
        String description,
        @DecimalMax(value = "99999.99", message = "Price is not valid")
        @DecimalMin(value = "0.0", message = "Price is not valid")
        BigDecimal price,
        ProductType type) {
}
