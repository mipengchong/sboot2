package com.zj;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {

    @Test
    public void longAdd() throws Exception {
        LongAdder adder = new LongAdder();
        ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>();
        map.putIfAbsent("a", adder);
        map.forEach(3,
                (k, v) -> k +"-"+v,       // 转换器
                System.out::println);
        String[] arr = {"an", "ba", "daf", "ads", "ca"};
        Arrays.parallelPrefix(arr, (x, y) -> x+"+"+y);
        System.out.println(JSON.toJSONString(arr));
        CompletableFuture<String> content = CompletableFuture.completedFuture("abcds");
        // 同一线程执行的代码写在一起
        content.thenApplyAsync(x -> x).thenAcceptAsync(System.out::println);

        System.out.println("222");
        Thread.sleep(10000L);
    }
}
