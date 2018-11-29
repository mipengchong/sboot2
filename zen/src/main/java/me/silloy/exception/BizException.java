package me.silloy.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常类
 * @author huyang
 * @version 2018/8/30
 */

@Data
@NoArgsConstructor
public class BizException extends RuntimeException{
    protected int errCode;
    protected String errMessage;
    protected Object data;

    public BizException(int errCode, String errMessage, Throwable cause) {
        super(errMessage, cause);
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public BizException(ErrorCodeEnum errorEnum, Throwable cause) {
        this(errorEnum.getCode(), errorEnum.getMsg(), cause);
    }

    public BizException(ErrorCodeEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getMsg(), null);
    }

    public BizException(String errMessage) {
        this(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode(), errMessage, null);
    }

    public BizException(int errCode, String errMessage) {
        this(errCode, errMessage, null);
    }

    public BizException(int errCode, String errMessage, Object data) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.data = data;
    }
}
