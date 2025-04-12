package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class GetUserByIdResponseDto {
    private int uid;
    private String username;
    private int permissionId;
    private String roomNumber;

    public GetUserByIdResponseDto(int uid, String username, int permissionId, String roomNumber) {
        this.uid = uid;
        this.username = username;
        this.permissionId = permissionId;
        this.roomNumber = roomNumber;
    }
}
