package com.fstore.model.jdbc;

import lombok.Data;

@Data
public class UserJdbc {
    private Integer id;
    private String email;
    private String password;
    private String photo_url;
    private int roleId;
}
