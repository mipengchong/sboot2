package com.zj;

import cyclops.control.Eval;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class LazyTest {


    //传统写法
    private String contents;
    //

    private void loadContents() {
        try {
            this.contents = loadFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFromFile() throws IOException{
        return Files.readAllLines(Path.of("")).toString();
    }

    public String getContents() {
        if (contents == null)
            loadContents();
        return contents;
    }


    //lambda lazy写法
    private Supplier<String> contents0 = this::loadContents0;

    //lazy,and cache.
    private String loadContents0() {
        try {
            return loadFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getContents0() {
        return contents0.get();
    }


    //jdk8 记忆+懒加载
    public static <T> Supplier<T> memoizeSupplier(final Supplier<T> s) {
        final Map<Long, T> lazy = new ConcurrentHashMap<>();
        return () -> lazy.computeIfAbsent(1l, i -> s.get());
    }

//第三方 记忆➕懒加载的包。
//        <dependency>
//            <groupId>com.oath.cyclops</groupId>
//            <artifactId>cyclops</artifactId>
//            <version>10.0.4</version>
//        </dependency>

    //    private Supplier<String> contents= Memoize.memoizeSupplier(()->loadContents());
    private Eval<String> contentss = Eval.later(() -> loadContents0());

}
