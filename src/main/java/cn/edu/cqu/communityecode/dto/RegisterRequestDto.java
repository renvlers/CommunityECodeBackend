package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String phone;
    private String username;
    private String password;
    private int permission;
    private String roomNumber;
}
