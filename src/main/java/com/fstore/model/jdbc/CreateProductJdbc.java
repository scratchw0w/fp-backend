package com.fstore.model.jdbc;

import com.fstore.model.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductJdbc {
    private String title;
    private String description;
    private BigDecimal price;
    private String type;
    private String photo_url;
}
