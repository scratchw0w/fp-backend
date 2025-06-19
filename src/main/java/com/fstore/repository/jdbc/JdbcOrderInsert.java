package com.fstore.repository.jdbc;

import lombok.Getter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Getter
public class JdbcOrderInsert {
    private final SimpleJdbcInsert insert;

    public JdbcOrderInsert(DataSource dataSource) {
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("\"order\"")
                .usingGeneratedKeyColumns("id");
    }
}
