package com.fstore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductImageDto(
        @JsonProperty("photo_url")
        String photoUrl,
        byte[] content) {
}
