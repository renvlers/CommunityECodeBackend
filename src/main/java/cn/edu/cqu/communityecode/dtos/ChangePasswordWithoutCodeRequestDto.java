package cn.edu.cqu.communityecode.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordWithoutCodeRequestDto {
    private int uid;
    private String originalPassword;
    private String newPassword;
}
