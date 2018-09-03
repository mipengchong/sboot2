package com.zj.webflux;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/24
 * Time: 21:02
 * Description: MAIN
 */
public class MethodTest {

    @Test
    public void stream() {
        Integer[] intArray = {1,2,3,4,5,6,7,8,9};
        List<Integer> integerList  = Arrays.asList(intArray);
        List<Integer> reslutList = new ArrayList<>();
        integerList.parallelStream().peek(reslutList::add).forEachOrdered(System.out::print);

        System.out.println();

        reslutList.forEach(System.out::print);
        System.out.println();
        reslutList.stream().forEachOrdered(System.out::print);
    }
}
