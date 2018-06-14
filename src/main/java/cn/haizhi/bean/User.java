package cn.haizhi.bean;

import lombok.Data;

@Data
public class User {
    private String userId;

    private String username;

    private String password;

    private Byte age;

    private Byte gender;

    private String phone;

    }