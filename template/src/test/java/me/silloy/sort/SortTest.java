package me.silloy.sort;

import com.alibaba.fastjson.JSON;
import com.zj.zjtools.number.RandomUtils;
import org.junit.Test;

import java.util.Random;
import java.util.stream.Stream;

public class SortTest {

    public int[] bubble(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    public int[] select(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int min = array[i];
            int flag = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < min) {
                    min = array[j];
                    flag = j;
                }
                if (flag != i) {
                    array[flag] = array[i];
                    array[i] = min;
                }
            }
        }
        return array;
    }


    @Test
    public void sort() {
        int[] array = RandomUtils.getRandom(10);
        long start = System.currentTimeMillis();
        System.out.println(JSON.toJSONString(bubble(array)));
        System.out.println(JSON.toJSONString(select(array)));
        System.out.println("sort: " + (System.currentTimeMillis() - start));
    }


    public void swap(int[] s, int i, int j) {
        int temp = s[i];
        s[j] = s[i];
        s[i] = temp;
    }
}
