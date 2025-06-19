package com.fstore.repository;


import com.fstore.model.auth.User;

public interface UserRepository {
    User findByEmail(String email);

    boolean update(String email, User user);
}
