package com.fstore.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        String allowedHosts,
        String staticImagePath) {
}
