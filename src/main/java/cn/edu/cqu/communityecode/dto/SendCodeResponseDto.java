package cn.edu.cqu.communityecode.dto;

import lombok.Data;

@Data
public class SendCodeResponseDto {
    private boolean success; // 是否请求成功
    private String reason; // 如果请求失败，返回失败原因

    public SendCodeResponseDto(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
