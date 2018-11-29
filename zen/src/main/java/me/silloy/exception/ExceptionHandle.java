package me.silloy.exception;

import me.silloy.domain.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionHandle {
    static Logger logger= LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(BizException.class)
    public Response bizExceptionHandler(BizException ex) {
        logger.error("bizException: " + ex.getMessage());
        return Response.getInstance().setErrorCode(ex.getErrCode()).setErrorMessage(ex.getErrMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response runtimeException(RuntimeException ex) {
        logger.error("RuntimeException: " + ex.getMessage());
        ex.printStackTrace();
        return Response.getInstance().setErrorCode(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode())
                .setErrorMessage(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response runtimeException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException: " + ex.getMessage());
        ex.printStackTrace();
        return Response.getInstance().setErrorCode(ErrorCodeEnum.ILLEGAL_ARGUMENT_ERROR.getCode())
                .setErrorMessage(ErrorCodeEnum.ILLEGAL_ARGUMENT_ERROR.getMsg());
    }

    @ExceptionHandler(NullPointerException.class)
    public Response otherExceptionHandler(NullPointerException ex) {
        StackTraceElement[] els = ex.getStackTrace();
        String msg = "系统执行错误:";
        if (els != null && els.length > 0) {
            StackTraceElement row = els[0];
            msg += row.getClassName() + " : methodName:" + row.getMethodName() + " -> line number" + row.getLineNumber() + "::";
        }
        msg += ex.getMessage();
        logger.error("NullPointerException: " + msg);
        return Response.getInstance().setErrorCode(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode())
                .setErrorMessage(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Response otherExceptionHandler(Exception ex) {
        logger.error("otherException: " + ex.getMessage());
        ex.printStackTrace();
        return Response.getInstance().setErrorCode(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode())
                .setErrorMessage(ErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getMsg());
    }
}
