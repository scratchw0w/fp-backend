package com.fstore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateOrderResponseDto(@JsonProperty("new_id") int id) {
}
