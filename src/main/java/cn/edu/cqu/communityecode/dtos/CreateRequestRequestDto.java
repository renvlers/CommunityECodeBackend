package cn.edu.cqu.communityecode.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRequestRequestDto {
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;
    private String guestName;
    private String guestPhone;
    private int ownerId;
}
