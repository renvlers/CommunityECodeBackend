package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    private String phone;
    private String verificationCode;
    private String newPassword;
}
