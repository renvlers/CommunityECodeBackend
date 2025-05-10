package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String phone;
    private String password;
}
