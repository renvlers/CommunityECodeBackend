package cn.edu.cqu.communityecode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllowRequestDto {
    private String requestCode;
    private int entrance;
}
