package me.silloy.util.qiniuUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2017/6/19
 * Time: 13:13
 * CopyRight: Zhouji
 */
public class ImageHandler {

    static Logger log = LoggerFactory.getLogger(ImageHandler.class);
    public final static String domainOfBucket = QiniuInfo.domain;
    public final static String bucket = QiniuInfo.bucket;
    public final static String pipeline = QiniuInfo.pipeline;
    public final static String notifyUrl = QiniuInfo.notifyUrl;
    public final static Zone zone = Zone.zone1();


    /**
     * 获取token
     *
     * @return
     */
    public static String uploadToken(String bucket) {
        long expireSeconds = 3600 * 24 * 7;
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        return QiniuInfo.getAuth().uploadToken(bucket, null, expireSeconds, putPolicy);
    }

    /**
     * <p>下载文件</p>
     *
     * @param key             文件在七牛空间中的key
     * @param expireInSeconds 下载链接有效时间
     * @return the string
     * @author SuShaohua
     */
    public static String downloadImage(String key, Long expireInSeconds) {
        try {
            return QiniuInfo.getAuth().privateDownloadUrl(String.format("%s/%s", domainOfBucket, URLEncoder.encode(key, "utf-8")),
                    Optional.ofNullable(expireInSeconds).orElse(60L));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>下载文件</p>
     *
     * @param key 文件名称
     * @return the string
     * @author SuShaohua
     */
    public static String downloadImage(String key) {
        return downloadImage(key, null);
    }


    /**
     * <p>下载开放空间文件</p>
     *
     * @param key 文件名称
     * @return the string
     * @author SuShaohua
     */
    public static String generateOpenImage(String key) {
        try {
            return String.format("%s/%s", domainOfBucket, URLEncoder.encode(key, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * <p>删除七牛空间文件</p>
     *
     * @param key 文件名称
     * @return 1 删除成功
     * @author SuShaohua
     */
    public static Integer deleteImage(String key) {
        Configuration cfg = new Configuration(zone);
        BucketManager bucketManager = new BucketManager(QiniuInfo.getAuth(), cfg);
        try {
            Response response = bucketManager.delete(bucket, key);
            return 1;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.info(String.valueOf(ex.code()));
            log.info(ex.response.toString());
        }
        return 0;
    }


    public static Integer deleteImage(String key, String bucket) {
        Configuration cfg = new Configuration(zone);
        BucketManager bucketManager = new BucketManager(QiniuInfo.getAuth(), cfg);
        try {
            Response response = bucketManager.delete(bucket, key);
            return 1;
        } catch (QiniuException ex) {
            ex.printStackTrace();
        }
        return 0;
    }


    /**
     * <p>文件上传</p>
     *
     * @param inputStream 文件流 key 文件名称，如不提供则使用文件hash值
     * @param key         the key
     * @return key 上传成功后的key值
     * @author SuShaohua
     */
    public static String uploadImage(InputStream inputStream, String key) {
        try {

//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(image, "PNG", os);
//            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            Configuration cfg = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(cfg);
            String upToken = QiniuInfo.getAuth().uploadToken(bucket, key);

            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            log.info("response: " + JSON.toJSONString(response));
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info(r.toString());
            try {
                log.info(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * <p>文件上传</p>
     *
     * @param inputStream 文件流 key 文件名称，如不提供则使用文件hash值
     * @param key         the key
     * @return key 上传成功后的key值
     * @author SuShaohua
     */
    public static String uploadFile(InputStream inputStream, String key) {
        try {

//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(image, "PNG", os);
//            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            Configuration cfg = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(cfg);
            String upToken = QiniuInfo.getAuth().uploadToken(bucket, key);

            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            log.info("response: " + JSON.toJSONString(response));
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info(r.toString());
            try {
                log.info(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>文件上传</p>
     *
     * @param   key 文件名称，如不提供则使用文件hash值
     * @param key         the key
     * @return key 上传成功后的key值
     * @author SuShaohua
     */
    public static String uploadFile(File file, String key) {
        try {

//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(image, "PNG", os);
//            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            Configuration cfg = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(cfg);
            String upToken = QiniuInfo.getAuth().uploadToken(bucket, key);
            Response response = uploadManager.put(file, key, upToken);
            log.info("response: " + JSON.toJSONString(response));
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info(r.toString());
            try {
                log.info(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>文件上传</p>
     *
     * @param   key 文件名称，如不提供则使用文件hash值
     * @param key         the key
     * @return key 上传成功后的key值
     * @author SuShaohua
     */
    public static String uploadFile(String file, String key) {
        try {

//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(image, "PNG", os);
//            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            Configuration cfg = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(cfg);
            String upToken = QiniuInfo.getAuth().uploadToken(bucket, key);
            Response response = uploadManager.put(file, key, upToken);
            log.info("response: " + JSON.toJSONString(response));
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info(r.toString());
            try {
                log.info(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * <p>少量文件压缩持久化</p>
     *
     * @param images 需要压缩的文件列表
     * @param key    产生的zip包名称，建议提供
     * @return key string
     * @author SuShaohua
     */
    public static String zipImage(List<QiniuMedia> images, String key) {
        try {
            OperationManager om = new OperationManager(QiniuInfo.getAuth(), new Configuration(zone));
            StringBuffer buf = new StringBuffer();

            buf.append("mkzip/2")
                    .append("/encoding/").append(UrlSafeBase64.encodeToString("utf-8"));
            for (QiniuMedia img : images) {
                buf
                        .append("/url/").append(UrlSafeBase64.encodeToString(img.getDownloadImg()))
                        .append("/alias/").append(UrlSafeBase64.encodeToString(Optional.ofNullable(img.getAlias()).orElse(img.getKey())))
                        .append("\n");
            }
            buf.append("|saveas/").append(UrlSafeBase64.encodeToString(ImageHandler.bucket + ":" + key));

            String pfop = om.pfop(ImageHandler.bucket, images.get(0).getKey(), buf.toString(), ImageHandler.pipeline, ImageHandler.notifyUrl, true);
            log.info("pfop: " + pfop);
            return key;
        } catch (QiniuException e) {
            Response r = e.response;
            log.info(r.toString());
            try {
                log.info(r.bodyString());
            } catch (QiniuException e1) {
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * <p>大量文件压缩持久化</p>
     *
     * @param images      需要压缩的文件列表
     * @param zipFileName 产生的zip包名称，建议提供
     * @return key string
     * @author SuShaohua
     */
    public static String zipMountImage(List<QiniuMedia> images, String zipFileName) {

        try {
            StringBuffer buffer = new StringBuffer();
            for (QiniuMedia img : images) {
                buffer
                        .append("/url/").append(UrlSafeBase64.encodeToString(img.getDownloadImg()))
                        .append("/alias/").append(UrlSafeBase64.encodeToString(Optional.ofNullable(img.getAlias()).orElse(img.getKey())))
                        .append("\n");
            }

            InputStream inputStream = new ByteArrayInputStream(buffer.toString().getBytes());

            String index = "index_" + (zipFileName.contains(".") ? zipFileName.substring(0, zipFileName.indexOf(".")) : zipFileName);
            String key = uploadImage(inputStream, index);

            OperationManager om = new OperationManager(QiniuInfo.getAuth(), new Configuration(zone));
            StringBuffer buf = new StringBuffer();

            buf.append("mkzip/4")
                    .append("/encoding/").append(UrlSafeBase64.encodeToString("utf-8"))
                    .append("|saveas/").append(UrlSafeBase64.encodeToString(ImageHandler.bucket + ":" + zipFileName));

            String pfop = om.pfop(ImageHandler.bucket, key, buf.toString(), ImageHandler.pipeline, ImageHandler.notifyUrl, true);
            log.info("pfop: " + pfop);
            deleteImage(index);
            return zipFileName;
        } catch (QiniuException e) {
            Response r = e.response;
            log.info(r.toString());
            try {
                log.info(r.bodyString());
            } catch (QiniuException e1) {
            }
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 批量获取空间文件列表
     *
     * @param prefix 文件名前缀
     * @param limit  每次迭代的长度限制，最大1000，推荐值 1000
     */
    public static List<QiniuMedia> listImage(String prefix, Integer limit) {
        List<QiniuMedia> mediaList = new ArrayList<>();
        BucketManager bucketManager = new BucketManager(QiniuInfo.getAuth(), new Configuration(zone));
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                QiniuMedia media = new QiniuMedia();
                media.setKey(item.key);
                media.setHash(item.hash);
                media.setDownloadImg(generateOpenImage(item.key));
                mediaList.add(media);
            }
        }
        return mediaList;
    }
}
