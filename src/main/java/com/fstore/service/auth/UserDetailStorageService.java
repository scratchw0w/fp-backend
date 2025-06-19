package com.fstore.service.auth;

import com.fstore.model.auth.User;
import com.fstore.model.auth.UserDetailsEntity;
import com.fstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserDetailStorageService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User loadedUser = repository.findByEmail(email);
        return UserDetailsEntity.build(loadedUser);
    }
}
