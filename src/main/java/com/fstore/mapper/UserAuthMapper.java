package com.fstore.mapper;

import com.fstore.model.auth.User;
import com.fstore.model.dto.UserLoggedInResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserAuthMapper {
    @Mapping(target = "role", source = "role.title")
    UserLoggedInResponseDto toDto(User user);
}
