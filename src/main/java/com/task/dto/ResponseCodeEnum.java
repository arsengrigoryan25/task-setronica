package com.task.dto;

public enum ResponseCodeEnum {
    RETURN_TYPE_VOID("00"),
    SERVICE_NOT_FOUND("01"),
    NO_SUCH_METHOD("02"),
    INCORRECT_PARAMETERS_TYPE("03"),
    INCORRECT_PARAMETERS_COUNT("04"),
    ;

    private String code;

    ResponseCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}