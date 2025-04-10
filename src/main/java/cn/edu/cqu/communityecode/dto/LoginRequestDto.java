package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String phone;
    private String password;
    private int permission;
}
