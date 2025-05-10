package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String phone;
    private String username;
    private String password;
    private String roomNumber;
}
