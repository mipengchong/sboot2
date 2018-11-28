package me.silloy.service.impl;

import me.silloy.domain.RedisKV;
import me.silloy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisTemplate redisTemplate;


    private void setHashNumBatch(List<RedisKV<String, RedisKV<String,Long>>> redisKVS) {
        if (redisKVS == null) {
            return ;
        }

        redisTemplate.executePipelined((RedisConnection connection) -> {
            StringRedisSerializer serializer = new StringRedisSerializer();
            for (RedisKV<String, RedisKV<String, Long>> redisKV : redisKVS) {
                String key = redisKV.getK();
                byte[] keyByte = serializer.serialize(key);
                if (redisKV.getTtl() != null) {
                    connection.pExpireAt(keyByte,redisKV.getTtl());
                }
                connection.hIncrBy(keyByte, serializer.serialize(redisKV.getV().getK()), redisKV.getV().getV());
            }
            return null;
        });
    }


    private Long updGONumPipelined(String key, List<RedisKV<String,Long>> kvs) {
        redisTemplate.executePipelined(new SessionCallback<List<Object>>() {
            @Override
            public  List<Object> execute(RedisOperations operations) throws DataAccessException {
                for (RedisKV<String, Long> kv : kvs) {
                    operations.boundHashOps(key).increment(kv.getK(), kv.getV());
                }
                return operations.exec();
            }
        });
        return null;
    }
}
