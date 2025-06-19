package com.fstore.repository.jdbc;

import com.fstore.exception.ProductNotFoundException;
import com.fstore.exception.ProductNotUpdatedException;
import com.fstore.mapper.jdbc.JdbcProductMapper;
import com.fstore.model.Product;
import com.fstore.model.ProductType;
import com.fstore.model.jdbc.ProductJdbc;
import com.fstore.repository.ProductRepository;
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
public class JdbcProductRepository implements ProductRepository {
    private final static BeanPropertyRowMapper<ProductJdbc> ROW_MAPPER =
            new BeanPropertyRowMapper<>(ProductJdbc.class);

    private final NamedParameterJdbcOperations jdbc;
    private final JdbcProductMapper mapper;
    private final JdbcProductInsert insert;

    @Override
    public List<Product> findAll() {
        List<ProductJdbc> jdbcs = jdbc.query("""
                            SELECT * FROM product;
                        """,
                ROW_MAPPER);

        return mapper.toDomain(jdbcs);
    }

    @Override
    public List<Product> findAllByType(ProductType type) {
        List<ProductJdbc> jdbcs = jdbc.query("""
                            SELECT * FROM product
                            WHERE type = :type;
                        """,
                Map.of("type", type.toString()),
                ROW_MAPPER);

        return mapper.toDomain(jdbcs);
    }

    @Override
    public Product findById(int id) {
        List<ProductJdbc> jdbcs = jdbc.query("""
                            SELECT * FROM product
                            WHERE id = :id;
                        """,
                Map.of("id", id),
                ROW_MAPPER);

        if (jdbcs.isEmpty()) {
            throw new ProductNotFoundException("Product with id %s was not found".formatted(id));
        }

        return mapper.toDomain(jdbcs.get(0));
    }

    @Override
    public int insert(Product product) {
        return insert.getInsert()
                .executeAndReturnKey(new BeanPropertySqlParameterSource(mapper.toCreateJdbc(product)))
                .intValue();
    }

    @Override
    public boolean update(int id, Product product) {
        ProductJdbc productJdbc = mapper.toJdbc(id, product);
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(productJdbc);
        String updatedFields = Stream.of(
                        "title", "description", "price", "type", "photo_url")
                .filter(param -> nonNull(params.getValue(param)))
                .map(param -> param + "=:" + param)
                .collect(Collectors.joining(","));

        int updated = jdbc.update("""
                            UPDATE product
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
    public boolean remove(int id) {
        int updated = jdbc.update("""
                            DELETE FROM product
                            WHERE id = :id;
                        """,
                Map.of("id", id));

        return updated != 0;
    }
}
