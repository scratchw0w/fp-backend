package com.fstore.service;

import com.fstore.exception.ProductNotRemovedException;
import com.fstore.model.Product;
import com.fstore.model.ProductImage;
import com.fstore.model.ProductType;
import com.fstore.properties.ApplicationProperties;
import com.fstore.repository.OrderRepository;
import com.fstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ApplicationProperties properties;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StorageService storageService;

    public List<Product> getAllProducts(Optional<ProductType> type) {
        log.info("Getting all products");

        List<Product> products = type.isEmpty()
                ? productRepository.findAll()
                : productRepository.findAllByType(type.get());

        return products.stream()
                .map(product -> {
                    var image = getImageByPath(product.image().photoUrl());
                    return product.withImage(image);
                })
                .toList();
    }

    public Product getProductById(int id) {
        log.info("Getting product with id: %s".formatted(id));

        Product product = productRepository.findById(id);
        ProductImage image = getImageByPath(product.image().photoUrl());

        return product.withImage(image);
    }

    private ProductImage getImageByPath(String path) {
        try {
            byte[] content = storageService.load(path, properties.staticImagePath());
            return new ProductImage(path, content);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "An exception occurred during reading content from: %s"
                            .formatted(path), exception);
        }
    }

    public int createProduct(Product product) {
        log.info("Creating product with title: {}", product.title());
        return productRepository.insert(product);
    }

    public boolean uploadImage(int productId, String fileName, byte[] content) {
        log.info("Uploading image for product with id: %s".formatted(productId));

        Product product = productRepository.findById(productId);

        String generatedImageName = storageService.upload(fileName, properties.staticImagePath(), content);

        boolean isUpdated = productRepository.update(
                productId, product.withImage(new ProductImage(generatedImageName, null)));

        if (!isUpdated) {
            // when not updated, should be removed
            storageService.delete(
                    generatedImageName,
                    properties.staticImagePath());
        }

        if (isUpdated && isImagePresent(product)) {
            storageService.delete(
                    product.image().photoUrl(),
                    properties.staticImagePath());
        }

        return true;
    }

    private boolean isImagePresent(Product product) {
        return Objects.nonNull(product.image())
                && Objects.nonNull(product.image().photoUrl());
    }

    public boolean updateProduct(int productId, Product product) {
        log.info("Updating product with id: %s".formatted(productId));
        return productRepository.update(productId, product);
    }

    @Transactional
    public boolean deleteProduct(int id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id);

        orderRepository.removeByProductId(product.id());
        boolean isRemoved = productRepository.remove(id);
        if (!isRemoved) {
            throw new ProductNotRemovedException(
                    "An exception occurred while removing product with id: %s".formatted(id));
        }

        if (!isImagePresent(product)) {
            return true;
        }

        boolean isImageRemoved = storageService.delete(product.image().photoUrl(), properties.staticImagePath());
        if (!isImageRemoved) {
            throw new ProductNotRemovedException(
                    "An exception occurred while removing product image, for product with id: %s and image: %s"
                            .formatted(id, product.image().photoUrl()));
        }

        return true;
    }


}
