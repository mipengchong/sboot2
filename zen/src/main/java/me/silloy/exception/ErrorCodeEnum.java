package me.silloy.exception;

import lombok.Getter;

public enum ErrorCodeEnum {

    /**
     系统异常
     */
    SYSTEM_DEFAULT_ERROR(-1, "对不起，系统发生了异常"),

    /**
     * 参数异常
     */
    ILLEGAL_ARGUMENT_ERROR(10000, "参数异常"),
    ;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private int code;
    @Getter
    private String msg;
}
