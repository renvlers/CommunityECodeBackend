package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String phone;
    private String username;
    private String password;
    private int permission;
    private String roomNumber;

    public RegisterRequestDto(String phone, String username, String password, int permission, String roomNumber) {
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.roomNumber = roomNumber;
    }
}
