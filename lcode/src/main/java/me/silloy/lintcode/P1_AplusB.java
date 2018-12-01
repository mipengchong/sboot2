package me.silloy.lintcode;

/**
 * 题目：给出两个整数 aa 和 bb , 求他们的和。
 * 说明
 * a和b都是 32位 整数么？
 * <p>
 * 是的
 * 我可以使用位运算符么？
 * <p>
 * 当然可以
 * 样例
 * 如果 a=1 并且 b=2，返回3。
 * <p>
 * 挑战
 * 显然你可以直接 return a + b，但是你是否可以挑战一下不这样做？（不使用++等算数运算符）
 * <p>
 * <p>
 * 思路 https://blog.csdn.net/wangyezi19930928/article/details/52516332
 * <p>
 * 考虑二进制数的情况（5＝101，17＝10001）：
 * 仍然分3步：
 *     1. 忽略进位，对应各位数字相加，得到10100；
 *     2. 记录进位，本例中只有最后一位相加时产生进位1，进位值为10（二进制）；
 *     3. 按照第1步中的方法将进位值与第1步结果相加，得到最终结果10110，正好是十进制数22的二进制表示。
 * <p>
 * 接下来把上述二进制加法3步计算法用位运算替换：
 *     第1步（忽略进位）：0＋0＝0，0＋1＝1，1＋0＝0，1＋1＝0，典型的异或运算。
 *     第2步：很明显，只有1＋1会向前产生进位1，相对于这一数位的进位值为10，而10＝(1&1)<<1。
 *     第3步：将第1步和第2步得到的结果相加，其实又是在重复上述2步，直到不再产生进位为止。
 */
public class P1_AplusB {

    /*
     * param a: The first integer
     * param b: The second integer
     * return: The sum of a and b
     */
    public int aplusb(int a, int b) {
        // write your code here, try to do it without arithmetic operators.
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int sum, i;
        i = a ^ b;
        sum = (a & b) << 1;
        return aplusb(sum, i);
    }

}
