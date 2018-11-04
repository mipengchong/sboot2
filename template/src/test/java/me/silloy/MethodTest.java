package me.silloy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import org.apache.commons.collections4.ListUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MethodTest {

    /**
     * apache partition, guava partition 类似
     */
    @Test
    public void apachePartition() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        List<List<Integer>> subSets = ListUtils.partition(intList, 3);

        List<Integer> lastPartition = subSets.get(2);
        List<Integer> expectedLastPartition = Lists.<Integer> newArrayList(7, 8);
        assertThat(subSets.size(), equalTo(3));
        assertThat(lastPartition, equalTo(expectedLastPartition));
    }



    @Test
    public void javaPartition() {
        List<Integer> intList = Lists.newArrayList(1, 2, 9, 4, 5, 6, 7, 8);
        Map<Boolean, List<Integer>> groups =
                intList.stream().collect(Collectors.partitioningBy(s -> s > 6));
        List<List<Integer>> subSets = new ArrayList<>(groups.values());
        System.out.println(JSON.toJSONString(subSets));
    }
}
