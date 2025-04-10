package cn.edu.cqu.communityecode.controller;

import cn.edu.cqu.communityecode.dto.*;
import cn.edu.cqu.communityecode.entity.User;
import cn.edu.cqu.communityecode.repository.UserRepository;
import cn.edu.cqu.communityecode.service.CodeService;
import cn.edu.cqu.communityecode.util.PasswordUtil;
import cn.edu.cqu.communityecode.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send_code")
    public Response<SendCodeResponseDto> sendCode(@RequestBody SendCodeRequestDto sendCodeRequestDto) {
        String phone = sendCodeRequestDto.getPhone();
        try {
            boolean sendSuccessfully = codeService.sendVerificationCode(phone);
            if(!sendSuccessfully) throw new Exception();
            return new Response<>("验证码发送成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>("验证码发送失败", null);
        }
    }

    @PostMapping("/register")
    public Response<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        try{
            User user = new User();
            user.setPhone(registerRequestDto.getPhone());
            user.setUsername(registerRequestDto.getUsername());
            user.setPassword(PasswordUtil.sha256(registerRequestDto.getPassword()));
            user.setPermissionId(registerRequestDto.getPermission());
            user.setRoomNumber(registerRequestDto.getRoomNumber());
            userRepository.save(user);
            List<User> result = userRepository.findUserByPhone(registerRequestDto.getPhone());
            int uid = result.getLast().getUid();
            return new Response<>("账号注册成功", new RegisterResponseDto(uid));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>("账号注册失败", null);
        }
    }

    @PostMapping("/login")
    public Response<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            String phone = loginRequestDto.getPhone();
            List<User> users = userRepository.findUserByPhone(phone);
            if(users.isEmpty()) throw new Exception("用户不存在");
            User user = users.getLast();
            if(!user.getPermissionId().equals(loginRequestDto.getPermission())) throw new Exception("权限验证失败");
            String password = PasswordUtil.sha256(loginRequestDto.getPassword());
            if(!password.equals(user.getPassword())) throw new Exception("密码错误");
            return new Response<>("登录成功", new LoginResponseDto(user.getUid()));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }
}
