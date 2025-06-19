package com.fstore.controller;

import com.fstore.exception.OrderNotFoundException;
import com.fstore.exception.OrderNotUpdatedException;
import com.fstore.exception.OrderStatusAlreadyCompletedException;
import com.fstore.exception.dto.ErrorDto;
import com.fstore.mapper.OrderMapper;
import com.fstore.model.Order;
import com.fstore.model.UserAuth;
import com.fstore.model.dto.*;
import com.fstore.service.OrderService;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {
    private final OrderMapper mapper;
    private final OrderService service;

    @Operation(summary = "Receive all orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders were successfully received",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
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
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<Order> orders = service.getAllOrders();
        return ResponseEntity.ok(mapper.toDtos(orders));
    }

    @Operation(summary = "Receive order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was successfully received",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(mapper.toDto(service.getOrderById(id)));
    }

    @Operation(summary = "Receive order by id and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was successfully received",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/info")
    public ResponseEntity<OrderResponseDto> getOrderByIdAndEmail(
            @RequestParam(name = "id") int id,
            @RequestParam(name = "email") String email) {
        return ResponseEntity.ok(mapper.toDto(service.getOrderByIdAndEmail(id, email)));
    }

    @Operation(summary = "Create order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was successfully created",
                    content = @Content(schema = @Schema(implementation = CreateOrderResponseDto.class))),
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
    public ResponseEntity<CreateOrderResponseDto> createOrder(
            @Valid @RequestBody CreateOrderRequestDto request) {
        int newId = service.createOrder(mapper.toDomain(request));
        return ResponseEntity.ok(new CreateOrderResponseDto(newId));
    }

    @Operation(summary = "Update order status by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was successfully updated",
                    content = @Content(schema = @Schema(implementation = BooleanResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Order status is already maximum",
                    content = @Content(schema = @Schema(implementation = OrderStatusAlreadyCompletedException.class))),
            @ApiResponse(responseCode = "400", description = "Order was not updated",
                    content = @Content(schema = @Schema(implementation = OrderNotUpdatedException.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<BooleanResponseDto> processStatusToNextStep(
            @PathVariable int id) {
        boolean isSucceed = service.processToNextStep(id);
        return ResponseEntity.ok(new BooleanResponseDto(isSucceed));
    }

    @Operation(summary = "Update order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was successfully updated",
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
    @PatchMapping("/{id}")
    public ResponseEntity<BooleanResponseDto> updateOrder(
            @PathVariable int id,
            @RequestAttribute UserAuth auth,
            @RequestBody UpdateOrderRequestDto request) {
        log.info("User: %s is updating order with id: %s"
                .formatted(auth.email(), id));

        boolean isSucceed = service.updateOrder(id, mapper.toDomain(request));

        return ResponseEntity.ok(new BooleanResponseDto(isSucceed));
    }

    @Operation(summary = "Delete order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was successfully removed",
                    content = @Content(schema = @Schema(implementation = BooleanResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Product was not removed",
                    content = @Content(schema = @Schema(implementation = OrderNotFoundException.class))),
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
    public ResponseEntity<BooleanResponseDto> deleteOrder(
            @PathVariable int id,
            @RequestAttribute UserAuth auth) {
        log.info("User: %s is deleting product with id: %s"
                .formatted(auth.email(), id));

        boolean isSucceed = service.deleteOrder(id);
        return ResponseEntity.ok(new BooleanResponseDto(isSucceed));
    }
}
