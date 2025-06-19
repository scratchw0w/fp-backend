package com.fstore.mapper.jdbc;

import com.fstore.model.auth.User;
import com.fstore.model.jdbc.UserJdbc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface JdbcUserMapper {
    @Mapping(source = "photo_url", target = "photoUrl")
    User toDomain(UserJdbc jdbc);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "user.photoUrl", target = "photo_url")
    UserJdbc toJdbc(String email, User user);
}
