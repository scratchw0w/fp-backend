package com.fstore.service;

import com.fstore.model.EmailRequest;
import com.fstore.model.Order;
import com.fstore.model.OrderStatus;
import com.fstore.properties.EmailProperties;
import com.fstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.fstore.model.EmailType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final EmailSender emailSender;
    private final EmailProperties properties;
    private final OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        log.info("Getting all orders");
        return orderRepository.findAll();
    }

    public Order getOrderById(int id) {
        log.info("Getting order with id: {}", id);
        return orderRepository.findById(id);
    }

    public Order getOrderByIdAndEmail(int id, String email) {
        log.info("Getting order with id: {} and email: {}", id, email);
        return orderRepository.findByIdAndEmail(id, email);
    }

    public int createOrder(Order order) {
        log.info("Creating order with email: {}", order.email());

        Order orderToCreate = order
                .withStatus(OrderStatus.CREATED)
                .withTimestamp(LocalDateTime.now());

        int newId = orderRepository.create(orderToCreate);

        Order createdOrder = orderToCreate.withId(newId);

        emailSender.send(
                new EmailRequest(
                        order.email(),
                        Map.of(
                                "orderId", String.valueOf(createdOrder.id()),
                                "orderStatus", createdOrder.status().toString()),
                        CREATE_ORDER_USER_NOTIFICATION)
        );

        emailSender.send(
                new EmailRequest(
                        properties.adminEmail(),
                        Map.of(
                                "userEmail", order.email(),
                                "orderId", String.valueOf(createdOrder.id())),
                        CREATE_ORDER_ADMIN_NOTIFICATION)
        );

        return newId;
    }

    public boolean processToNextStep(int id) {
        log.info("Processing status to the next step, for order with id: {}", id);

        Order order = orderRepository.findById(id);
        OrderStatus nextStatus = order.status().goNext();

        boolean isSucceed = orderRepository.update(id, nextStatus);

        if (isSucceed) {
            emailSender.send(
                    new EmailRequest(
                            order.email(),
                            Map.of(
                                    "orderId", String.valueOf(id),
                                    "orderStatus", nextStatus.toString()),
                            UPDATE_ORDER_STATUS_USER_NOTIFICATION));
        }

        return isSucceed;
    }

    public boolean updateOrder(int id, Order order) {
        log.info("Updating order with id:{}", id);
        return orderRepository.update(id, order);
    }

    public boolean deleteOrder(int id) {
        log.info("Deleting order with id: {}", id);
        return orderRepository.remove(id);
    }
}
