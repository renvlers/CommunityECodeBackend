package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

@Data
public class LoginResponseDto {
    private int uid;

    public LoginResponseDto(int uid) {
        this.uid = uid;
    }
}
