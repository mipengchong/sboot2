package me.silloy.lintcode;

/**
 * 描述
 * 查找斐波纳契数列中第 N 个数。
 * <p>
 * 所谓的斐波纳契数列是指：
 * <p>
 * 前2个数是 0 和 1 。
 * 第 i 个数是第 i-1 个数和第i-2 个数的和。
 * 斐波纳契数列的前10个数字是：
 * <p>
 * 0, 1, 1, 2, 3, 5, 8, 13, 21, 34 ...
 * <p>
 * The Nth fibonacci number won't exceed the max value of signed 32-bit integer in the test cases.
 * <p>
 * 递归操作会超时，不通过
 * https://blog.csdn.net/kkdd2013/article/details/51982827
 */
public class P366_FibonacciArrays {

    public long fibonacci(int n) {
        if (n <= 2) {
            return n - 1;
        }

        long first = 0;
        long second = 1;

        for (int i = 3; i <= n; i++) {
            second = first + second;
            first = second - first;
        }
        return second;
    }

}
