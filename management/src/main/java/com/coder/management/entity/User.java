package com.coder.management.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    // 辅助加密密码
    private String salt;
    private String email;
    private int type;
    private int status;
    // 激活码
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
