package com.fstore.mapper.jdbc;

import com.fstore.model.Order;
import com.fstore.model.jdbc.CreateOrderJdbc;
import com.fstore.model.jdbc.OrderJdbc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface JdbcOrderMapper {
    List<Order> toDomain(List<OrderJdbc> jdbcs);

    @Mapping(source = "phone_number", target = "phoneNumber")
    @Mapping(source = "delivery_time", target = "deliveryTime")
    @Mapping(source = "product_id", target = "productId")
    Order toDomain(OrderJdbc jdbc);

    @Mapping(source = "phoneNumber", target = "phone_number")
    @Mapping(source = "deliveryTime", target = "delivery_time")
    @Mapping(source = "productId", target = "product_id")
    CreateOrderJdbc toCreateJdbc(Order order);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "order.phoneNumber", target = "phone_number")
    @Mapping(source = "order.deliveryTime", target = "delivery_time")
    @Mapping(source = "order.productId", target = "product_id")
    OrderJdbc toJdbc(int id, Order order);
}
