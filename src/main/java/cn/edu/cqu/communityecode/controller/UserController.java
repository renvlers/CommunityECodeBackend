package cn.edu.cqu.communityecode.controller;

import cn.edu.cqu.communityecode.dto.RegisterRequestDto;
import cn.edu.cqu.communityecode.dto.RegisterResponseDto;
import cn.edu.cqu.communityecode.dto.SendCodeRequestDto;
import cn.edu.cqu.communityecode.dto.SendCodeResponseDto;
import cn.edu.cqu.communityecode.service.CodeService;
import cn.edu.cqu.communityecode.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/send_code")
    public Response<SendCodeResponseDto> sendCode(@RequestBody SendCodeRequestDto sendCodeRequestDto) {
        String phone = sendCodeRequestDto.getPhone();
        try {
            boolean sendSuccessfully = codeService.sendVerificationCode(phone);
            if(!sendSuccessfully) throw new Exception();
            return new Response<>("验证码发送成功", new SendCodeResponseDto(true, null));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>("验证码发送失败", new SendCodeResponseDto(false, "验证码发送失败"));
        }
    }

    @PostMapping("/register")
    public Response<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {

    }
}
