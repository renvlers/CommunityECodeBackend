package cn.edu.cqu.communityecode.controller;

import cn.edu.cqu.communityecode.dto.*;
import cn.edu.cqu.communityecode.entity.User;
import cn.edu.cqu.communityecode.service.UserService;
import cn.edu.cqu.communityecode.util.HashUtil;
import cn.edu.cqu.communityecode.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/send_code")
    public Response<SendCodeResponseDto> sendCode(@RequestBody SendCodeRequestDto sendCodeRequestDto) {
        String phone = sendCodeRequestDto.getPhone();
        try {
            boolean sendSuccessfully = userService.sendVerificationCode(phone);
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
            if(userService.getUserByPhone(registerRequestDto.getPhone()) != null) throw new Exception("用户已存在");
            User user = new User();
            user.setPhone(registerRequestDto.getPhone());
            user.setUsername(registerRequestDto.getUsername());
            user.setPassword(HashUtil.sha256(registerRequestDto.getPassword()));
            user.setPermissionId(registerRequestDto.getPermission());
            user.setRoomNumber(registerRequestDto.getRoomNumber());
            userService.createNewUser(user);
            User result = userService.getUserByPhone(registerRequestDto.getPhone());
            int uid = result.getUid();
            return new Response<>("账号注册成功", new RegisterResponseDto(uid));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public Response<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            String phone = loginRequestDto.getPhone();
            User user = userService.getUserByPhone(phone);
            if(user == null) throw new Exception("用户不存在");
            if(!user.getPermissionId().equals(loginRequestDto.getPermission())) throw new Exception("权限验证失败");
            String password = HashUtil.sha256(loginRequestDto.getPassword());
            if(!password.equals(user.getPassword())) throw new Exception("密码错误");
            return new Response<>("登录成功", new LoginResponseDto(user.getUid()));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @PutMapping("/change_password")
    public Response<ChangePasswordResponseDto> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        try {
            String phone = changePasswordRequestDto.getPhone();
            String verificationCode = changePasswordRequestDto.getVerificationCode();
            String password = HashUtil.sha256(changePasswordRequestDto.getNewPassword());
            User user = userService.getUserByPhone(phone);
            if(user == null) throw new Exception("用户不存在");
            if(!userService.checkVerificationCode(phone, verificationCode)) throw new Exception("验证码错误");
            if(password.equals(user.getPassword())) throw new Exception("新密码不能与原密码一致");
            userService.changePassword(user, password);
            return new Response<>("密码修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @GetMapping("/get_user?uid={uid}")
    public Response<GetUserByIdResponseDto> getUserById(@PathVariable int uid) {
        try {
            User user = userService.getUserById(uid);
            if(user == null) throw new Exception("用户不存在");
            int id = user.getUid();
            String username = user.getUsername();
            int permissionId = user.getPermissionId();
            String roomNumber = user.getRoomNumber();
            return new Response<>("用户查找成功", new GetUserByIdResponseDto(id, username, permissionId, roomNumber));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }
}
