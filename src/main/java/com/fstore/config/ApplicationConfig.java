package com.fstore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fstore.mapper.jdbc.JdbcOrderMapper;
import com.fstore.mapper.jdbc.JdbcProductMapper;
import com.fstore.mapper.jdbc.JdbcRoleMapper;
import com.fstore.mapper.jdbc.JdbcUserMapper;
import com.fstore.properties.KafkaProperties;
import com.fstore.repository.OrderRepository;
import com.fstore.repository.ProductRepository;
import com.fstore.repository.UserRepository;
import com.fstore.repository.jdbc.*;
import com.fstore.service.EmailSender;
import com.fstore.service.EmailSenderImplementation;
import com.fstore.service.ResourceStorageService;
import com.fstore.service.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfig {
    @Bean
    public JdbcProductInsert productInsert(DataSource dataSource) {
        return new JdbcProductInsert(dataSource);
    }

    @Bean
    public ProductRepository jdbcProductRepository(
            NamedParameterJdbcOperations jdbc,
            JdbcProductMapper mapper,
            JdbcProductInsert insert) {
        return new JdbcProductRepository(jdbc, mapper, insert);
    }

    @Bean
    public JdbcOrderInsert orderInsert(DataSource dataSource) {
        return new JdbcOrderInsert(dataSource);
    }

    @Bean
    public OrderRepository jdbcOrderRepository(
            NamedParameterJdbcOperations jdbc,
            JdbcOrderMapper mapper,
            JdbcOrderInsert insert) {
        return new JdbcOrderRepository(jdbc, mapper, insert);
    }

    @Bean
    public UserRepository jdbcUserRepository(
            NamedParameterJdbcOperations jdbc,
            JdbcUserMapper userMapper,
            JdbcRoleMapper roleMapper) {
        return new JdbcUserRepository(jdbc, userMapper, roleMapper);
    }

    @Bean
    public StorageService resourceStorageService() {
        return new ResourceStorageService();
    }

    @Bean
    public EmailSender emailSender(
            ObjectMapper mapper,
            KafkaProperties properties,
            KafkaTemplate<String, String> template) {
        return new EmailSenderImplementation(mapper, properties, template);
    }
}
