package com.fstore.mapper;

import com.fstore.model.Order;
import com.fstore.model.dto.CreateOrderRequestDto;
import com.fstore.model.dto.OrderResponseDto;
import com.fstore.model.dto.UpdateOrderRequestDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    Order toDomain(UpdateOrderRequestDto requestDto);
    Order toDomain(CreateOrderRequestDto requestDto);
    OrderResponseDto toDto(Order order);
    List<OrderResponseDto> toDtos(List<Order> orders);
}
