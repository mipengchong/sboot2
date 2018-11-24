package com.zj;

import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamTest {
    /**
     * Collectors.groupingBy 分组1
     */
    @Test
    public void testGrouping() {
        List<String> items = Arrays.asList("apple", "banana", "apple", "orange", "banana");
        Collector<Object, ?, Map<Object, Long>> objectMapCollector = Collectors.groupingBy(Function.identity(), Collectors.counting());
        Map<Object, Long> collect = items.stream().collect(objectMapCollector);
        System.out.println(collect);
        //{orange=1, banana=2, apple=2}
    }

    /**
     * Collectors.groupingBy 分组2
     */
    @Test
    public void testGrouping2() {
        List<Student> students = Arrays.asList(new Student("apple", "男", 10.0),
                new Student("banana", "男", 10.0),
                new Student("orange", "男", 20.0),
                new Student("pipe", "女", 40.0),
                new Student("pinck", "女", 80.0)
        );

        //根据sex分组
        Map<String, List<Student>> collect = students.stream().collect(Collectors.groupingBy(Student::getSex));
        System.out.println(collect);
        //{
        // 女=[Student{name='pipe', sex='女', money=40.0}, Student{name='pinck', sex='女', money=80.0}],
        // 男=[Student{name='apple', sex='男', money=10.0}, Student{name='banana', sex='男', money=10.0}, Student{name='orange', sex='男', money=20.0}]
        // }

        //根据sex分组，然后对money求和
        Map<String, Double> collect1 = students.stream().collect(Collectors.groupingBy(Student::getSex, Collectors.summingDouble(Student::getMoney)));
        System.out.println(collect1);
        //{女=120.0, 男=40.0}
    }

    /**
     * list、set
     */
    @Test
    public void testGrouping3() {
        List<Student> students = Arrays.asList(new Student("apple", "男", 10.0),
                new Student("banana", "男", 10.0),
                new Student("orange", "男", 20.0),
                new Student("pipe", "女", 40.0),
                new Student("pinck", "女", 80.0)
        );
        Map<String, List<Student>> collect1 = students.stream().collect(Collectors.groupingBy(Student::getSex));
        System.out.println(collect1);
        //{
        // 女=[Student{name='pipe', sex='女', money=40.0}, Student{name='pinck', sex='女', money=80.0}],
        // 男=[Student{name='apple', sex='男', money=10.0}, Student{name='banana', sex='男', money=10.0}, Student{name='orange', sex='男', money=20.0}]
        // }

        Map<String, List<Double>> collect2 = students.stream().collect(Collectors.groupingBy(Student::getSex, Collectors.mapping(Student::getMoney, Collectors.toList())));
        System.out.println(collect2);
        //{女=[40.0, 80.0], 男=[10.0, 10.0, 20.0]}

        Map<String, Set<Double>> collect3 = students.stream().collect(Collectors.groupingBy(Student::getSex, Collectors.mapping(Student::getMoney, Collectors.toSet())));
        System.out.println(collect3);
        //{女=[40.0, 80.0], 男=[10.0, 20.0]}
    }

    /**
     * toMap 转成map
     */
    @Test
    public void tesToMap() {
        List<Student> students = Arrays.asList(new Student("apple", "男", 10.0),
                new Student("banana", "男", 10.0),
                new Student("orange", "男", 20.0),
                new Student("pipe", "女", 40.0),
                new Student("pinck", "女", 80.0)
        );


        Map<String, Double> collect = students.stream().collect(Collectors.toMap(Student::getName, Student::getMoney));
        System.out.println(collect);
        //{orange=20.0, banana=10.0, apple=10.0, pinck=80.0, pipe=40.0}
    }

    /**
     * toMap 转成map  key重复
     */
    @Test
    public void tesToMap2() {
        List<Student> students = Arrays.asList(new Student("apple", "男", 10.0),
                new Student("banana", "男", 10.0),
                new Student("orange", "男", 20.0),
                new Student("pipe", "女", 40.0),
                new Student("pinck", "女", 80.0)
        );
        /*
        java.lang.IllegalStateException: Duplicate key apple
        at java.util.stream.Collectors.lambda$throwingMerger$0(Collectors.java:133)
        at java.util.HashMap.merge(HashMap.java:1254)
        at java.util.stream.Collectors.lambda$toMap$58(Collectors.java:1320)
         */
//        Map<Double,String> collect2 = students.stream().collect(Collectors.toMap(Student::getMoney, Student::getName));
//        System.out.println(collect2);

        Map<Double, String> collect2 = students.stream().collect(Collectors.toMap(Student::getMoney, Student::getName, (oldValue, newValue) -> newValue));
        System.out.println(collect2);
        //{80.0=pinck, 40.0=pipe, 20.0=orange, 10.0=banana}  key重复用新值

    }

    /**
     * toMap 转成map 排序
     */
    @Test
    public void tesToMap3() {
        List<Student> students = Arrays.asList(new Student("apple", "男", 10.0),
                new Student("banana", "男", 10.0),
                new Student("orange", "男", 20.0),
                new Student("pipe", "女", 40.0),
                new Student("pinck", "女", 80.0)
        );

        LinkedHashMap<String, Double> collect = students.stream().sorted(Comparator.comparing(Student::getMoney).reversed())
                .collect(Collectors.toMap(Student::getName, Student::getMoney, (oldValue, newValue) -> newValue, LinkedHashMap::new));
        System.out.println(collect);
        //{pinck=80.0, pipe=40.0, orange=20.0, apple=10.0, banana=10.0}
    }

    @Data
    public class Student {
        private String name;
        private String Sex;
        private Double money;

        public Student(String name, String sex, Double money) {
            this.name = name;
            Sex = sex;
            this.money = money;
        }
    }
}
