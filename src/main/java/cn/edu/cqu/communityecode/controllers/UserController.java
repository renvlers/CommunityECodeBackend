package cn.edu.cqu.communityecode.controllers;

import cn.edu.cqu.communityecode.dtos.*;
import cn.edu.cqu.communityecode.entities.User;
import cn.edu.cqu.communityecode.services.UserService;
import cn.edu.cqu.communityecode.utils.HashUtil;
import cn.edu.cqu.communityecode.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /** 发送短信验证码 */
    @PostMapping("/send_code")
    public ResponseEntity<Response<SendCodeResponseDto>> sendCode(
            @RequestBody SendCodeRequestDto sendCodeRequestDto) {

        String phone = sendCodeRequestDto.getPhone();
        try {
            boolean ok = userService.sendVerificationCode(phone);
            if (!ok)
                throw new Exception("验证码发送失败");

            return ResponseEntity.ok(new Response<>("验证码发送成功", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 用户注册 */
    @PostMapping("/register")
    public ResponseEntity<Response<RegisterResponseDto>> register(
            @RequestBody RegisterRequestDto dto) {

        try {
            if (userService.checkIfUserExistsByPhone(dto.getPhone()) != null)
                throw new Exception("用户已存在");

            User user = new User();
            user.setPhone(dto.getPhone());
            user.setUsername(dto.getUsername());
            user.setPassword(HashUtil.sha256(dto.getPassword()));
            user.setRoomNumber(dto.getRoomNumber());
            userService.createNewUser(user);

            int uid = userService.getUserByPhone(dto.getPhone()).getUid();
            return ResponseEntity.ok(
                    new Response<>("账号注册成功", new RegisterResponseDto(uid)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 用户登录 */
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponseDto>> login(
            @RequestBody LoginRequestDto dto) {

        try {
            User user = userService.getUserByPhone(dto.getPhone());
            if (user == null)
                throw new Exception("用户不存在");
            String pwd = HashUtil.sha256(dto.getPassword());
            if (!pwd.equals(user.getPassword()))
                throw new Exception("密码错误");

            return ResponseEntity.ok(
                    new Response<>("登录成功", new LoginResponseDto(user.getUid())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 修改密码 */
    @PutMapping("/change_password")
    public ResponseEntity<Response<ChangePasswordResponseDto>> changePassword(
            @RequestBody ChangePasswordRequestDto dto) {

        try {
            String phone = dto.getPhone();
            String code = dto.getVerificationCode();
            String newPw = HashUtil.sha256(dto.getNewPassword());

            User user = userService.getUserByPhone(phone);
            if (user == null)
                throw new Exception("用户不存在");
            if (!userService.checkVerificationCode(phone, code))
                throw new Exception("验证码错误");
            if (newPw.equals(user.getPassword()))
                throw new Exception("新密码不能与原密码一致");

            userService.changePassword(user, newPw);
            return ResponseEntity.ok(new Response<>("密码修改成功", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 根据 UID 查询用户 */
    @GetMapping("/get_user")
    public ResponseEntity<Response<GetUserByIdResponseDto>> getUserById(@RequestParam int uid) {

        try {
            User user = userService.getUserById(uid);
            if (user == null)
                throw new Exception("用户不存在");

            GetUserByIdResponseDto data = new GetUserByIdResponseDto(
                    user.getUid(),
                    user.getUsername(),
                    user.getPhone(),
                    user.getRoomNumber());
            return ResponseEntity.ok(new Response<>("用户查找成功", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }
}
