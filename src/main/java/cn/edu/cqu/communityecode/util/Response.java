package cn.edu.cqu.communityecode.util;

import lombok.Data;

@Data
public class Response<T> {
    private String message;
    private T data;

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
