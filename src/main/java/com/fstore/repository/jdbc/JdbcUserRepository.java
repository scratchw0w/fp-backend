package com.fstore.repository.jdbc;

import com.fstore.exception.RoleNotFoundException;
import com.fstore.exception.UserNotUpdatedException;
import com.fstore.mapper.jdbc.JdbcRoleMapper;
import com.fstore.mapper.jdbc.JdbcUserMapper;
import com.fstore.model.auth.User;
import com.fstore.model.jdbc.RoleJdbc;
import com.fstore.model.jdbc.UserJdbc;
import com.fstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private static final BeanPropertyRowMapper<UserJdbc> USER_ROW_MAPPER =
            new BeanPropertyRowMapper<>(UserJdbc.class);
    private static final BeanPropertyRowMapper<RoleJdbc> ROLE_ROW_MAPPER =
            new BeanPropertyRowMapper<>(RoleJdbc.class);

    private final NamedParameterJdbcOperations jdbc;
    private final JdbcUserMapper userMapper;
    private final JdbcRoleMapper roleMapper;

    @Override
    public User findByEmail(String email) {
        List<UserJdbc> userJdbcs = jdbc.query("""
                            SELECT * FROM "user"
                            WHERE email = :email;
                        """,
                Map.of("email", email),
                USER_ROW_MAPPER);

        if (userJdbcs.isEmpty()) {
            throw new UsernameNotFoundException(
                    "User with email: %s, was not found".formatted(email));
        }

        UserJdbc userJdbc = userJdbcs.get(0);

        List<RoleJdbc> roleJdbc = jdbc.query("""
                            SELECT * FROM role
                            WHERE id = :roleId;
                        """,
                Map.of("roleId", userJdbc.getRoleId()),
                ROLE_ROW_MAPPER);

        if (roleJdbc.isEmpty()) {
            throw new RoleNotFoundException(
                    "Role for user: %s, was not found".formatted(email));
        }

        User user = userMapper.toDomain(userJdbc);
        return user.withRole(roleMapper.toDomain(roleJdbc.get(0)));
    }

    @Override
    public boolean update(String email, User user) {
        UserJdbc userJdbc = userMapper.toJdbc(email, user);
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(userJdbc);
        String updatedFields = Stream.of("photo_url")
                .filter(param -> nonNull(params.getValue(param)))
                .map(param -> param + "=:" + param)
                .collect(Collectors.joining(","));

        int updated = jdbc.update("""
                            UPDATE "user"
                            SET %s
                            WHERE email = :email;
                        """.formatted(updatedFields),
                params);

        if (updated == 0) {
            throw new UserNotUpdatedException(
                    "User with email = %s was not updated.".formatted(email));
        }

        return true;
    }
}
