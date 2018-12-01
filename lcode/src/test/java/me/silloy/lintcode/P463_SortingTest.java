package me.silloy.lintcode;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class P463_SortingTest {

    @Test
    void sortIntegers() {
        P463_Sorting sorting = new P463_Sorting();
        int[] A = new int[]{1,2,4,6,4,2,7,5};
        sorting.sortIntegers(A);
        System.out.println(JSON.toJSONString(A));
    }
}