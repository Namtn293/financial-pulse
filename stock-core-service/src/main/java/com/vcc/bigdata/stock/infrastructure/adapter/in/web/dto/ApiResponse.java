package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        int code,
        String message,
        T data,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(true, 200, "Success", data, Instant.now());
    }

    public static <T> ApiResponse<T> ok(T data, String message){
        return new ApiResponse<>(true, 200, message, data, Instant.now());
    }

    public static <T> ApiResponse<T> error(int code, String message){
        return new ApiResponse<>(true, code, message, null, Instant.now());
    }
}
