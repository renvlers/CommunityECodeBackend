package cn.edu.cqu.communityecode.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AiAssistantService {
    @Autowired
    private DataSource dataSource;

    public String getTextFromAiByResult(Integer uid, String userMessage, String result) throws Exception {
        String apiUrl = "https://api.chatanywhere.tech/v1/chat/completions";
        String apiKey = "sk-Xl66Sp43zZON1myJgq8xTh2zvLhiXEG7uMySc5KMx9nKExh2";
        String prompt = "现有一位UID为" + uid + "的业主询问了以下内容：" + userMessage + "\n\n" + "已知根据SQL的查询结果如下: \n\n" +
                result + "\n\n" +
                "请返回对此的解读，你应该返回一个JSON文本，其中字段`success`表示返回是否成功，字段`message`表示返回的具体内容，**不要在JSON对象以外返回包括Markdown格式标记在内的任何字符！！！**"
                +
                "你可能需要用到以下信息：1. status表示状态，其中为0表示已过期，为1表示已允许，为2表示已拒绝，为3表示已撤回，如果出现其他情况，统一记作未处理; 2. 用户权限值为1表示业主，0表示物管";
        prompt = prompt.replace("\n", "\\n");
        prompt = prompt.replace("\"", "\\\"");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        String jsonBody = """
                {
                  "model": "gpt-4.1-mini",
                  "messages": [{"role": "user", "content": "%s"}],
                  "temperature": 0.7
                }
                """.formatted(prompt);

        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                jsonBody, okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new Exception("AI接口响应失败");
        }
    }

    public String sendPromptToAi(Integer uid, String userMessage) throws Exception {
        String apiUrl = "https://api.chatanywhere.tech/v1/chat/completions";
        String apiKey = "sk-Xl66Sp43zZON1myJgq8xTh2zvLhiXEG7uMySc5KMx9nKExh2";
        String prompt = "现有一位UID为" + uid + "的业主询问了以下内容：" + userMessage + '\n'
                + """
                        请基于该业主的内容，生成相应的SQL语句，用于查询，请注意一个业主不能查询属于其他业主的访客登记请求或访客访问记录

                        以下是我的数据库表设计，供参考：
                        ```sql
                        -- ----------------------------
                        -- 表 entrance 结构
                        -- ----------------------------
                        DROP TABLE IF EXISTS `entrance`;
                        CREATE TABLE `entrance` (
                            `id` int NOT NULL,
                            `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

                        -- ----------------------------
                        -- 表 entrance 记录
                        -- 如果查询的信息包含入口，请确保最终 SQL 语句的执行结果给出的是入口名称而不是 ID
                        -- ----------------------------
                        BEGIN;
                        INSERT INTO `entrance` (`id`, `name`) VALUES (0, '无');
                        INSERT INTO `entrance` (`id`, `name`) VALUES (1, '东大门');
                        INSERT INTO `entrance` (`id`, `name`) VALUES (2, '南大门');
                        INSERT INTO `entrance` (`id`, `name`) VALUES (3, '西大门');
                        INSERT INTO `entrance` (`id`, `name`) VALUES (4, '北大门');
                        COMMIT;
                        ```

                        ```sql
                        -- ----------------------------
                        -- 表 guest_record 结构
                        -- 该表用于记录已经使用或失效的请求，登记后尚未使用的请求请在 guest_request 中查询
                        -- status 表示状态，其中为 0 表示已过期，为 1 表示已同意，为 2 表示已拒绝，为 3 表示已撤回
                        -- 你可以直接返回状态的 ID，因为没有为状态建立单独的表
                        -- ----------------------------
                        DROP TABLE IF EXISTS `guest_record`;
                        CREATE TABLE `guest_record` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `enter_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `leave_time` datetime NOT NULL,
                            `guest_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `guest_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `entrance` int NOT NULL,
                            `owner_id` int NOT NULL,
                            `request_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `status` int NOT NULL DEFAULT '0',
                            PRIMARY KEY (`id`),
                            KEY `entrance` (`entrance`),
                            KEY `owner_id` (`owner_id`),
                            CONSTRAINT `guest_record_ibfk_1` FOREIGN KEY (`entrance`) REFERENCES `entrance` (`id`),
                            CONSTRAINT `guest_record_ibfk_3` FOREIGN KEY (`owner_id`) REFERENCES `user` (`uid`)
                        ) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                        ```

                        ```sql
                        -- ----------------------------
                        -- 表 guest_request 结构
                        -- 该表用于记录创建后尚未使用的登记，且后端已设置定时清理过期登记请求的任务，因此超出离开时间的登记请在 guest_record 中查询
                        -- ----------------------------
                        DROP TABLE IF EXISTS `guest_request`;
                        CREATE TABLE `guest_request` (
                            `request_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `enter_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `leave_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `guest_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `guest_phone` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `owner_id` int NOT NULL,
                            `hash` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            PRIMARY KEY (`request_code` DESC) USING BTREE,
                            KEY `owner_id` (`owner_id`),
                            CONSTRAINT `guest_request_ibfk_2` FOREIGN KEY (`owner_id`) REFERENCES `user` (`uid`)
                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                        ```

                        ```sql
                        -- ----------------------------
                        -- 表 user 结构
                        -- 注意：permission_id 值为 1 表示业主，为 0 表示物管
                        -- ----------------------------
                        DROP TABLE IF EXISTS `user`;
                        CREATE TABLE `user` (
                            `uid` int NOT NULL AUTO_INCREMENT,
                            `phone` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `permission_id` int NOT NULL,
                            `room_number` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            PRIMARY KEY (`uid`),
                            UNIQUE KEY `username` (`username`),
                            UNIQUE KEY `uq_users_phone` (`phone`),
                            KEY `permission_id` (`permission_id`)
                        ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                        ```

                        你应该返回一个JSON文本，其中字段`isSql`表示返回的内容是否为SQL语句；字段`message`表示返回的具体内容，如果`isSql`为`true`，则表示SQL语句，否则以文本形式表示不提供SQL语句的原因，**不要在JSON对象以外返回包括Markdown格式标记在内的任何字符！！！**
                        **注意：**
                            1. 如果用户的提问无法转化为查询或是涉及到询问其他业主的访客登记信息，则不应该提供SQL
                            2. isSql为false返回的内容应该直接面向最终用户，不要出现诸如"SQL"之类的技术名词
                        """;
        prompt = prompt.replace("\n", "\\n");
        prompt = prompt.replace("\"", "\\\"");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        String jsonBody = """
                {
                  "model": "gpt-4.1-mini",
                  "messages": [{"role": "user", "content": "%s"}],
                  "temperature": 0.7
                }
                """.formatted(prompt);

        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                jsonBody, okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new Exception("AI接口响应失败");
        }
    }

    public String executeSqlQuery(String sql) throws SQLException, JsonProcessingException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        }
        // 使用简单的方式将结果转换为JSON字符串
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.findAndRegisterModules(); // 支持Java 8日期时间类型
        return mapper.writeValueAsString(resultList);
    }
}
