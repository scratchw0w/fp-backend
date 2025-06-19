package com.fstore.model.jdbc;

import lombok.Data;

import java.util.Date;

@Data
public class OrderJdbc {
    private int id;
    private String name;
    private String email;
    private String phone_number;
    private String address;
    private String comment;
    private Date delivery_time;
    private String status;
    private Date timestamp;
    private int product_id;
}
