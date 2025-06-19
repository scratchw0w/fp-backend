package com.fstore.repository;

import com.fstore.model.Product;
import com.fstore.model.ProductType;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();

    List<Product> findAllByType(ProductType type);

    Product findById(int id);

    int insert(Product product);

    boolean update(int id, Product product);

    boolean remove(int id);
}
