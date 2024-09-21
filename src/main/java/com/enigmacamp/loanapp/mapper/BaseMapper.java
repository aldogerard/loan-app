package com.enigmacamp.loanapp.mapper;

import com.enigmacamp.loanapp.base.BaseResponse;

public class BaseMapper {
    public static BaseResponse<?> mapToBaseResponse(String message, Integer statusCode, Object data) {
        return BaseResponse.builder()
                .message(message)
                .statusCode(statusCode)
                .data(data)
                .build();
    }
}
