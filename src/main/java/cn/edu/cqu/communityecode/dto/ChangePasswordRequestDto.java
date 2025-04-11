package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    private String phone;
    private String verificationCode;
    private String newPassword;
}
