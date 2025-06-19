package com.fstore.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties(
        String kafkaUrl,
        String emailTopic) {
}
