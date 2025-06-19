package com.fstore.model;

import java.util.Map;

public record EmailRequest(
        String recipient,
        Map<String, String> params,
        EmailType type) {
}
