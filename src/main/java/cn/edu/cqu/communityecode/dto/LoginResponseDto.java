package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private int uid;

    public LoginResponseDto(int uid) {
        this.uid = uid;
    }
}
