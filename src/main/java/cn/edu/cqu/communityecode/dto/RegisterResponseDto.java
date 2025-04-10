package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class RegisterResponseDto {
    private int uid;

    public RegisterResponseDto(int uid) {
        this.uid = uid;
    }
}
