package me.silloy.common;

import java.text.MessageFormat;

/**
 * redis key 常量
 *
 * @author 李洪春
 * @create 2018/6/29 下午2:05
 **/
public class RedisKeys {
    public static final String ORDER_SPU_KEY = "ORDER_SPU_";
    public static final String GO_TO_DB_KEY = "GO_TO_DB";
    public static final String PER_COUNT_PRE = "PER_COUNT_";
    public static final String SPU_COUNT = "SPU_COUNT_TOTAL";
    public static final String SPU_MEMBER_COUNT = "SPU_MEMBER_COUNT_TOTAL";
    public static final String CATEGORY_CODE_LIST = "CATEGORY_CODE_LIST";

    public static final String ASYNC_EXPORT_KEY = "EXPORT:EXCEL:ASYNC:DATA";
    public static final String ASYNC_EXPORT_RUNNING_KEY = "EXPORT:EXCEL:ASYNC:RUNNING";
    public static final String ASYNC_EXPORT_UNION_KEY = "EXPORT:EXCEL:ASYNC:UNION:";

    /**
     * 活动商品已售数量
     * @param spuId
     * @param actId
     * @return
     */
    public static String getCountHashKey(Long spuId, Long actId) {
        return MessageFormat.format("{0}_{1}", spuId, actId);
    }

    /**
     * 集散地spu价格 map redis key
     * @param distributePlaceId
     * @return
     */
    public static String getDistributePlaceSpuPriceKey(Long distributePlaceId){return MessageFormat.format( "distributeplace:{0}:price:spu",distributePlaceId);}

    /**
     * 集散地sku价格 map redis key
     * @param distributePlaceId
     * @return
     */
    public static String getDistributePlaceSKuPriceKey(Long distributePlaceId){return MessageFormat.format("distributeplace:{0}:price:sku",distributePlaceId);}

    /**
     * 集散地spu在途采购量 map redis key
     * @param distributePlaceId
     * @return
     */
    public static String getDistributePlaceSpuCurrentQuantityKey(Long distributePlaceId){return MessageFormat.format("distributeplace:{0}:quantity:spu",distributePlaceId);}

    /**
     * 集散地sku在途采购量 map redis key
     * @param distributePlaceId
     * @return
     */
    public static String getDistributePlaceSkuCurrentQuantityKey(Long distributePlaceId){return MessageFormat.format("distributeplace:{0}:quantity:sku",distributePlaceId);}

    /**
     * 库存采购单2。0 入库锁
     * @param purchaseInventoryId
     * @return
     */
    public static String getPurchaseInventoryIntoStorage(Long purchaseInventoryId){return MessageFormat.format("purchaseinventory:tostorage:{0}:",purchaseInventoryId);}

    /**
     * 商品已售数
     * @param spuId
     * @return
     */
    public static String getSpuCountHashKey(Long spuId) {
        return MessageFormat.format("SPU_{0}", spuId);
    }

    /**
     * 已售人数
     * @param spuId
     * @return
     */
    public static String getMemberCountHashKey(Long spuId) {
        return MessageFormat.format("MSPU_{0}", spuId);
    }

    public static String genActOrderDayCountKey(Long communityId) {
        return MessageFormat.format("order_community_spu_count_{0}", communityId);
    }
    /**
     * 缓存活动内，用户购买数量
     * @param actId
     * @return
     */
    public static String genPerActUserCountKey(Long actId) {
        return new StringBuffer(PER_COUNT_PRE).append(actId).toString();
    }
    /**
     * 缓存活动内，用户购买数量
     * @return
     */
    public static String genSpuCountKey() {
        return SPU_COUNT;
    }
    /**
     * 缓存活动内，用户购买数量
     * @return
     */
    public static String genSpuMemberCountKey() {
        return SPU_MEMBER_COUNT;
    }
}
