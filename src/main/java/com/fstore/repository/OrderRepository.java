package com.fstore.repository;

import com.fstore.model.Order;
import com.fstore.model.OrderStatus;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll();

    Order findById(int id);

    Order findByIdAndEmail(int id, String email);

    int create(Order order);

    boolean update(int id, Order order);

    boolean update(int id, OrderStatus status);

    boolean remove(int id);

    boolean removeByProductId(int id);
}
