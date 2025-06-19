package com.fstore.mapper.jdbc;

import com.fstore.model.auth.Role;
import com.fstore.model.jdbc.RoleJdbc;
import org.mapstruct.Mapper;

@Mapper
public interface JdbcRoleMapper {
    Role toDomain(RoleJdbc jdbc);
}
