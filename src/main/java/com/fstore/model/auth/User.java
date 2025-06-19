package com.fstore.model.auth;

import lombok.With;

@With
public record User(
        int id,
        String email,
        String password,
        String photoUrl,
        Role role) {
}
