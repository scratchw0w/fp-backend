package com.fstore.controller;

import com.fstore.exception.ProductNotFoundException;
import com.fstore.exception.ProductNotRemovedException;
import com.fstore.exception.ProductNotUpdatedException;
import com.fstore.exception.dto.ErrorDto;
import com.fstore.mapper.ProductMapper;
import com.fstore.model.Product;
import com.fstore.model.UserAuth;
import com.fstore.model.dto.*;
import com.fstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductMapper mapper;
    private final ProductService service;

    @Operation(summary = "Receive all product, filtered or not filtered by type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products were successfully received",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(
            @RequestParam(name = "type", required = false) ProductTypeDto type) {
        List<Product> products = service.getAllProducts(Optional.ofNullable(mapper.toDomain(type)));
        return ResponseEntity.ok(mapper.toDtos(products));
    }

    @Operation(summary = "Receive product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was successfully received",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Product was not found",
                    content = @Content(schema = @Schema(implementation = ProductNotFoundException.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(mapper.toDto(service.getProductById(id)));
    }

    @Operation(summary = "Create product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was successfully created",
                    content = @Content(schema = @Schema(implementation = CreateProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping
    public ResponseEntity<CreateProductResponseDto> createProduct(
            @RequestAttribute UserAuth auth,
            @Valid @RequestBody CreateProductRequestDto request) {
        log.info("User: %s is creating new product"
                .formatted(auth.email()));

        int newId = service.createProduct(mapper.toDomain(request));

        return ResponseEntity.ok(new CreateProductResponseDto(newId));
    }

    @Operation(summary = "Upload image for specific product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product image was successfully uploaded",
                    content = @Content(schema = @Schema(implementation = BooleanResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<BooleanResponseDto> uploadProductImage(
            @PathVariable int id,
            @RequestAttribute UserAuth auth,
            @RequestParam("image") MultipartFile file) throws IOException {
        log.info("User: %s is uploading new image for product with id: %s"
                .formatted(auth.email(), id));

        boolean isSucceed = service.uploadImage(
                id,
                file.getOriginalFilename(),
                file.getBytes());

        return ResponseEntity.ok(new BooleanResponseDto(isSucceed));
    }

    @Operation(summary = "Update product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was successfully updated",
                    content = @Content(schema = @Schema(implementation = BooleanResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Product was not updated",
                    content = @Content(schema = @Schema(implementation = ProductNotUpdatedException.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BooleanResponseDto> updateProduct(
            @PathVariable int id,
            @RequestAttribute UserAuth auth,
            @Valid @RequestBody UpdateProductRequestDto request) {
        log.info("User: %s is updating product with id: %s"
                .formatted(auth.email(), id));

        boolean isSucceed = service.updateProduct(id, mapper.toDomain(request));
        return ResponseEntity.ok(new BooleanResponseDto(isSucceed));
    }

    @Operation(summary = "Delete product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was successfully removed",
                    content = @Content(schema = @Schema(implementation = BooleanResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Product was not removed",
                    content = @Content(schema = @Schema(implementation = ProductNotRemovedException.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BooleanResponseDto> deleteProduct(
            @RequestAttribute UserAuth auth,
            @PathVariable int id) {
        log.info("User: %s is deleting product with id: %s"
                .formatted(auth.email(), id));

        boolean isSucceed = service.deleteProduct(id);
        return ResponseEntity.ok(new BooleanResponseDto(isSucceed));
    }
}
