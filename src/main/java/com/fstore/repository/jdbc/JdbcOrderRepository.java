package com.fstore.repository.jdbc;

import com.fstore.exception.OrderNotFoundException;
import com.fstore.exception.OrderNotUpdatedException;
import com.fstore.exception.ProductNotUpdatedException;
import com.fstore.mapper.jdbc.JdbcOrderMapper;
import com.fstore.model.Order;
import com.fstore.model.OrderStatus;
import com.fstore.model.jdbc.CreateOrderJdbc;
import com.fstore.model.jdbc.OrderJdbc;
import com.fstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class JdbcOrderRepository implements OrderRepository {
    private static final BeanPropertyRowMapper<OrderJdbc> ROW_MAPPER =
            new BeanPropertyRowMapper<>(OrderJdbc.class);

    private final NamedParameterJdbcOperations jdbc;
    private final JdbcOrderMapper mapper;
    private final JdbcOrderInsert insert;

    @Override
    public List<Order> findAll() {
        List<OrderJdbc> jdbcs = jdbc.query("""
                            SELECT * FROM "order";
                        """,
                ROW_MAPPER);

        return mapper.toDomain(jdbcs);
    }

    @Override
    public Order findById(int id) {
        List<OrderJdbc> orderJdbc = jdbc.query("""
                            SELECT * FROM "order"
                            WHERE id = :id;
                        """,
                Map.of("id", id),
                ROW_MAPPER);

        if (orderJdbc.isEmpty()) {
            throw new OrderNotFoundException("Order with id %s was not found".formatted(id));
        }

        return mapper.toDomain(orderJdbc.get(0));
    }

    @Override
    public Order findByIdAndEmail(int id, String email) {
        List<OrderJdbc> orderJdbc = jdbc.query("""
                            SELECT * FROM "order"
                            WHERE id = :id AND email = :email;
                        """,
                Map.of("id", id,
                        "email", email),
                ROW_MAPPER);

        if (orderJdbc.isEmpty()) {
            throw new OrderNotFoundException("Order with id %s and email %s was not found".formatted(id, email));
        }

        return mapper.toDomain(orderJdbc.get(0));
    }

    @Override
    public int create(Order order) {
        CreateOrderJdbc createOrderJdbc = mapper.toCreateJdbc(order);

        return jdbc.queryForObject("""
                                    INSERT INTO "order" (name, email, phone_number, address, comment, delivery_time, status, timestamp, product_id)
                                    VALUES (:name, :email, :phone_number, :address, :comment, :delivery_time, :status, :timestamp, :product_id)
                                    RETURNING id;
                                """,
                        Map.of(
                                "name", createOrderJdbc.getName(),
                                "email", createOrderJdbc.getEmail(),
                                "phone_number", createOrderJdbc.getPhone_number(),
                                "address", createOrderJdbc.getAddress(),
                                "comment", createOrderJdbc.getComment(),
                                "delivery_time", createOrderJdbc.getDelivery_time(),
                                "status", createOrderJdbc.getStatus(),
                                "timestamp", createOrderJdbc.getTimestamp(),
                                "product_id", createOrderJdbc.getProduct_id()),
                        Integer.class)
                .intValue();
    }

    @Override
    public boolean update(int id, Order order) {
        OrderJdbc orderJdbc = mapper.toJdbc(id, order);
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(orderJdbc);
        String updatedFields = Stream.of(
                        "name", "email", "phone_number", "address", "comment", "delivery_time", "status", "product_id")
                .filter(param -> nonNull(params.getValue(param)))
                .map(param -> param + "=:" + param)
                .collect(Collectors.joining(","));

        int updated = jdbc.update("""
                            UPDATE "order"
                            SET %s
                            WHERE id = :id;
                        """.formatted(updatedFields),
                params);

        if (updated == 0) {
            throw new ProductNotUpdatedException("Product with id = %s was not updated.".formatted(id));
        }

        return true;
    }

    @Override
    public boolean update(int id, OrderStatus status) {
        int updated = jdbc.update("""
                            UPDATE "order"
                            SET status = :status
                            WHERE id = :id;
                        """,
                Map.of(
                        "id", id,
                        "status", status.name()));

        if (updated == 0) {
            throw new OrderNotUpdatedException(
                    "Order with id = %s was not updated.".formatted(id));
        }

        return true;
    }

    @Override
    public boolean remove(int id) {
        int updated = jdbc.update("""
                            DELETE FROM "order"
                            WHERE id = :id;
                        """,
                Map.of("id", id));

        return updated != 0;
    }

    @Override
    public boolean removeByProductId(int id) {
        jdbc.update("""
                            DELETE FROM "order"
                            WHERE product_id = :id;
                        """,
                Map.of("id", id));

        return true;
    }
}
