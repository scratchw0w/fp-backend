package com.fstore.repository.jdbc;

import lombok.Getter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Getter
public class JdbcProductInsert {
    private final SimpleJdbcInsert insert;

    public JdbcProductInsert(DataSource dataSource) {
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }
}
