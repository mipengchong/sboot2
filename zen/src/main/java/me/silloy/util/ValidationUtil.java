package me.silloy.util;

import me.silloy.exception.BizException;

import java.util.Collection;

/**
 * Created with Burt
 * Date: 2018/10/27
 * Time: 10:13 AM
 * 校验的工具类
 * 校验如果不通过，throw BizException
 */
public class ValidationUtil {

    /**
     * code默认值
     */
    public static final int DEFAULT_CODE = -1;

    /**
     * 校验对象非空
     * 如果是null，throw异常
     *
     * @param o
     * @param code
     * @param msg
     */
    public static void validNull(Object o, int code, String msg) {
        if (o == null) {
            throw new BizException(code, msg);
        }
    }

    /**
     * 校验对象非空
     * 如果是null，throw异常
     *
     * @param o
     * @param msg
     */
    public static void validNull(Object o, String msg) {
        if (o == null) {
            throw new BizException(DEFAULT_CODE, msg);
        }
    }

    /**
     * 校验String
     * 如果是空，throw异常
     *
     * @param s
     * @param msg
     */
    public static void validEmpty(String s, String msg) {
        if (s == null || s.trim().length() == 0) {
            throw new BizException(DEFAULT_CODE, msg);
        }
    }

    /**
     * 校验String
     * 如果是空，throw异常
     *
     * @param s
     * @param code
     * @param msg
     */
    public static void validEmpty(String s, int code, String msg) {
        if (s == null || s.trim().length() == 0) {
            throw new BizException(code, msg);
        }
    }

    /**
     * 校验数组
     * 如果是null，或长度为0，throw异常
     *
     * @param o
     * @param msg
     */
    public static void validArray(Object[] o, String msg) {
        if (o == null || o.length == 0) {
            throw new BizException(DEFAULT_CODE, msg);
        }
    }

    /**
     * 校验数组
     * 如果是null，或长度为0，throw异常
     *
     * @param o
     * @param code
     * @param msg
     */
    public static void validArray(Object[] o, int code, String msg) {
        if (o == null || o.length == 0) {
            throw new BizException(code, msg);
        }
    }

    /**
     * 校验集合
     * 如果是null，或长度为0，throw异常
     *
     * @param o
     * @param msg
     */
    public static void validCollection(Collection<?> o, String msg) {
        if (o == null || o.size() == 0) {
            throw new BizException(DEFAULT_CODE, msg);
        }
    }

    /**
     * 校验集合
     * 如果是null，或长度为0，throw异常
     *
     * @param o
     * @param code
     * @param msg
     */
    public static void validCollection(Collection<?> o, int code, String msg) {
        if (o == null || o.size() == 0) {
            throw new BizException(code, msg);
        }
    }
}
