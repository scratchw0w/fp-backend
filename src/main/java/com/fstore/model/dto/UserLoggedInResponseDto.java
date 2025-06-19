package com.fstore.model.dto;

public record UserLoggedInResponseDto(
        int id,
        String email,
        String photoUrl,
        String role) {
}
