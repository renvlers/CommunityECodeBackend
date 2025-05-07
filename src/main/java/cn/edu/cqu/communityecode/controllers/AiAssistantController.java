package cn.edu.cqu.communityecode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.cqu.communityecode.dtos.AiMessageDto;
import cn.edu.cqu.communityecode.dtos.AiResponseDto;
import cn.edu.cqu.communityecode.services.AiAssistantService;
import cn.edu.cqu.communityecode.services.UserService;
import cn.edu.cqu.communityecode.utils.Response;

import org.json.JSONObject;

@RestController
@RequestMapping("/ai")
public class AiAssistantController {
    @Autowired
    private AiAssistantService aiAssistantService;

    @Autowired
    private UserService userService;

    // TODO: 将方法名更改为sendMessageFromOwner
    @PostMapping("/send_message")
    ResponseEntity<Response<AiResponseDto>> sendMessage(@RequestBody AiMessageDto aiMessageDto) {
        try {
            userService.getUserById(aiMessageDto.getUid());
            String sqlResult = aiAssistantService.sendPromptToAi(aiMessageDto.getUid(), aiMessageDto.getMessage());
            JSONObject sqlResultObject = new JSONObject(sqlResult);
            JSONObject choice = sqlResultObject.getJSONArray("choices").getJSONObject(0);
            JSONObject messageObj = choice.getJSONObject("message");
            String content = messageObj.getString("content");
            JSONObject contentObj = new JSONObject(content);
            boolean isSql = contentObj.getBoolean("isSql");
            String message = contentObj.getString("message");
            if (!isSql)
                return ResponseEntity.ok().body(new Response<>("success", new AiResponseDto(true, message)));
            String executionResult = aiAssistantService.executeSqlQuery(message);
            String result = aiAssistantService.getTextFromAiByResult(aiMessageDto.getUid(), aiMessageDto.getMessage(),
                    executionResult);
            JSONObject resultObject = new JSONObject(result);
            JSONObject resultChoice = resultObject.getJSONArray("choices").getJSONObject(0);
            JSONObject resultMessageObj = resultChoice.getJSONObject("message");
            String resultContent = resultMessageObj.getString("content");
            JSONObject resultContentObj = new JSONObject(resultContent);
            boolean success = resultContentObj.getBoolean("success");
            String text = resultContentObj.getString("message");
            AiResponseDto aiResponseDto = new AiResponseDto(success, text);
            return ResponseEntity.ok(new Response<>("success", aiResponseDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Response<>(e.getMessage(), null));
        }
    }

    // TODO: 新增方法——sendMessageFromAdmin，用于物管人员提问
}
