package com.fstore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProductResponseDto(@JsonProperty("new_id") int newId) {
}
