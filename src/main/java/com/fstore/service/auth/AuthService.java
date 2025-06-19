package com.fstore.service.auth;

import com.fstore.model.auth.Role;
import com.fstore.model.auth.RoleType;
import com.fstore.model.auth.User;
import com.fstore.model.auth.UserDetailsEntity;
import com.fstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.fstore.service.utility.JwtUtils.generateJwtCookie;
import static com.fstore.service.utility.JwtUtils.getCleanJwtCookie;

@Service
@RequiredArgsConstructor
public class AuthService {
    //    private final EmailSender emailSender;
    private final UserRepository repository;
    private final AuthenticationManager authManager;

    public User loginFlow(String username, String password) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsEntity userEntity = (UserDetailsEntity) authentication.getPrincipal();

        String role = userEntity.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow();

        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getPhotoUrl(),
                new Role(0, RoleType.valueOf(role)));
    }

    public User checkWhoAmIFlow(String email) {
        return repository.findByEmail(email);
    }

    public String getJwtCookieFor(String email) {
        return generateJwtCookie(email).toString();
    }

    public String logoutFlow() {
        return getCleanJwtCookie().toString();
    }
}
