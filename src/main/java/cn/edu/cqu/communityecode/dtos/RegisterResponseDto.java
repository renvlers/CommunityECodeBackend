package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

@Data
public class RegisterResponseDto {
    private int uid;

    public RegisterResponseDto(int uid) {
        this.uid = uid;
    }
}
