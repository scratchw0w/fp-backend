package com.fstore.mapper.jdbc;

import com.fstore.model.Product;
import com.fstore.model.jdbc.CreateProductJdbc;
import com.fstore.model.jdbc.ProductJdbc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface JdbcProductMapper {
    List<Product> toDomain(List<ProductJdbc> jdbcs);

    @Mapping(source = "photo_url", target = "image.photoUrl")
    Product toDomain(ProductJdbc jdbc);


    CreateProductJdbc toCreateJdbc(Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "product.image.photoUrl", target = "photo_url")
    ProductJdbc toJdbc(int id, Product product);
}
