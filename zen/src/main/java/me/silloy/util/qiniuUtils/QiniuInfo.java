package me.silloy.util.qiniuUtils;

import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2017/6/19
 * Time: 13:18
 * CopyRight: Zhouji
 */
@Data
@AllArgsConstructor
@Component
public class QiniuInfo {
    private static String accessKey;
    private static String secretKey;

    public static String domain;
    public static String bucket;
    public static String pipeline;
    public static String notifyUrl;



    public static String getAccessKey() {
        return accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }


    @Value("${qiniu.accessKey}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Value("${qiniu.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${qiniu.domain}")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Value("${qiniu.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Value("${qiniu.pipeline}")
    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    @Value("${qiniu.notifyUrl}")
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public static Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }
}
