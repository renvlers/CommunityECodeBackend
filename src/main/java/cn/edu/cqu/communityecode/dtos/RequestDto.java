package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private String requestCode;
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;
    private String guestName;
    private String guestPhone;
    private int ownerId;
    private String hash;

    public RequestDto() {
    }

    public RequestDto(String requestCode, LocalDateTime enterTime, LocalDateTime leaveTime, String guestName,
            String guestPhone, int ownerId, String hash) {
        this.requestCode = requestCode;
        this.enterTime = enterTime;
        this.leaveTime = leaveTime;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.ownerId = ownerId;
        this.hash = hash;
    }
}
