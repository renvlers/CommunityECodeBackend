package cn.edu.cqu.communityecode.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefuseRequestDto {
    private String requestCode;
    private int entrance;
}
