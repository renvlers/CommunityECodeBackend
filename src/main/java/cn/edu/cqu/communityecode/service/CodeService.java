package cn.edu.cqu.communityecode.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class CodeService {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Cache<String, String> codeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public boolean sendVerificationCode(String phoneNumber) {
        StringBuilder code = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for(int i=0;i<6;i++) {
            code.append(random.nextInt(10));
        }
        String verificationCode = code.toString();
        String content = "【社区e码通】您的验证码为" + verificationCode + "，请在5分钟内完成验证。";

        String apiUrl = "https://api.smsbao.com/sms";
        String apiKey = "c15fae1c98854433addae5e356a1628b";
        String username = "jhy123_";
        String url = apiUrl + "?u=" + username + "&p=" + apiKey + "&m=" + phoneNumber + "&c=" + content;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间为30秒
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();

            boolean success = response.isSuccessful() && responseBody.startsWith("0");
            if(success) {
                codeCache.put(phoneNumber, verificationCode);
                return true;
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkVerificationCode(String phone, String inputCode) {
        String realCode = codeCache.getIfPresent(phone);
        return realCode != null && realCode.equals(inputCode);
    }

    public void removeVerificationCode(String phone) {
        codeCache.invalidate(phone);
    }
}