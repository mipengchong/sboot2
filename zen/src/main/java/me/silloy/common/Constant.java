package me.silloy.common;

public interface Constant {

    /**
     * jwt
     */
    String JWT_ID = "jwt";
    String JWT_SECRET = "son#2gs921@shssu~4H&pinsdf213*%pinwe8*koperation";
    long JWT_TTL = 30 * 24 * 60 * 60 * 1000L; // 毫秒
    long JWT_REFRESH_INTERVAL = 55 * 60 * 1000L; // millisecond
    long JWT_REFRESH_TTL = 2 * 365 * 24 * 60 * 60 * 1000L; // millisecond

    int TOKEN_EXPIRES_HOUR = 12;

    String CACHE_USER_ID_PREFIX = "operation_user_id_";
    int CACHE_USER_ID_TOKEN_TIME = 7;//用户token redis 时间

    int USER_SESSION_CACHE_TIME = 30;


    byte TYPE_OPERATION = 1;
    byte TYPE_MERCHART = 2;

    /**
     * 数据源
     */

    String DATASOURCE_ORDER = "order";
    String DATASOURCE_RETURN_ORDER = "return_order";
    String DISTRIBUTION_BIZ_TYPE = "DISTRIBUTION";
    String DATASOURCE_GOODS = "goods";
    String DATASOURCE_BI = "bi";
    String DATASOURCE_MEMBER = "member";
    String DATASOURCE_USER = "user";
    String DATASOURCE_DISTRIBUTION = "distribution";

    String CACHE_ACTIVITY_GOODS_KEY = "ACTIVITY_GOODS_";


    interface ACTIVITY_TIME {
        //活动开始时间9:30
        String ACTIVITY_BEGINTIME = "ACTIVITY_BEGINTIME";
        //活动结束时间20:30
        String ACTIVITY_ENDTIME = "ACTIVITY_ENDTIME";
        //活动开始结束相隔天数1
        String ACTIVITY_DAY = "ACTIVITY_DAY";
        //延迟生成活动开始（当天0，后天1）
        String ACTIVITY_DAY_DELAY = "ACTIVITY_DAY_DELAY";
    }
}
