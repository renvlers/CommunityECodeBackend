package cn.edu.cqu.communityecode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {
    private Integer id;
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;
    private String guestName;
    private String guestPhone;
    private String entrance;
    private Integer ownerId;
}
