package com.fstore.mapper;

import com.fstore.model.Product;
import com.fstore.model.ProductType;
import com.fstore.model.dto.CreateProductRequestDto;
import com.fstore.model.dto.ProductResponseDto;
import com.fstore.model.dto.ProductTypeDto;
import com.fstore.model.dto.UpdateProductRequestDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductType toDomain(ProductTypeDto typeDto);

    Product toDomain(CreateProductRequestDto requestDto);

    Product toDomain(UpdateProductRequestDto requestDto);

    List<ProductResponseDto> toDtos(List<Product> products);

    ProductResponseDto toDto(Product product);
}
