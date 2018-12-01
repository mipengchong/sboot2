package me.silloy.lintcode;

/**
 * 描述
 * 反转一个只有3位数的整数。
 *
 * 你可以假设输入一定是一个只有三位数的整数，这个整数大于等于100，小于1000。
 *
 */
public class P37_ReverseNum {

    public int reverseInteger(int number) {
        int a = number / 100;
        int b = number / 10;
        b -= a * 10;
        int c = number % 10;

        return a+(b*10)+(c*100);

    }
}
