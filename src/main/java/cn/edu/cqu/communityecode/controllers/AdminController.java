package cn.edu.cqu.communityecode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.cqu.communityecode.dtos.ChangePasswordResponseDto;
import cn.edu.cqu.communityecode.dtos.ChangePasswordWithoutCodeRequestDto;
import cn.edu.cqu.communityecode.dtos.LoginRequestDto;
import cn.edu.cqu.communityecode.dtos.LoginResponseDto;
import cn.edu.cqu.communityecode.entities.Admin;
import cn.edu.cqu.communityecode.services.AdminService;
import cn.edu.cqu.communityecode.utils.HashUtil;
import cn.edu.cqu.communityecode.utils.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /** 登录物管账号 */
    @PostMapping("/login_admin")
    public ResponseEntity<Response<LoginResponseDto>> loginAdmin(@RequestBody LoginRequestDto dto) {
        try {
            Admin admin = adminService.getAdminByPhone(dto.getPhone());
            if (admin == null)
                throw new Exception("此物管账号不存在");
            String pwd = HashUtil.sha256(dto.getPassword());
            if (!pwd.equals(admin.getPassword()))
                throw new Exception("密码错误");

            return ResponseEntity.ok(
                    new Response<>("登录成功", new LoginResponseDto(admin.getAid())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 使用原密码修改密码 */
    @PutMapping("/change_password_without_code")
    public ResponseEntity<Response<ChangePasswordResponseDto>> changePasswordWithoutCode(
            @RequestBody ChangePasswordWithoutCodeRequestDto dto) {
        try {
            int aid = dto.getUid();
            String originalPassword = HashUtil.sha256(dto.getOriginalPassword());
            String newPassword = HashUtil.sha256(dto.getNewPassword());

            Admin admin = adminService.getAdminByAid(aid);
            if (admin == null)
                throw new Exception("用户不存在");
            if (!admin.getPassword().equals(originalPassword))
                throw new Exception("原密码错误");
            if (newPassword.equals(admin.getPassword()))
                throw new Exception("新密码不能与原密码一致");

            adminService.changeAdminPassword(admin, newPassword);
            return ResponseEntity.ok(new Response<>("密码修改成功", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage(), null));
        }
    }

}
