package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

@Data
public class GetUserByIdResponseDto {
    private int uid;
    private String username;
    private String phone;
    private String roomNumber;

    public GetUserByIdResponseDto(int uid, String username, String phone, String roomNumber) {
        this.uid = uid;
        this.username = username;
        this.phone = phone;
        this.roomNumber = roomNumber;
    }
}
