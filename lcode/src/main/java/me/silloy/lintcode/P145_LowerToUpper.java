package me.silloy.lintcode;

/**
 * 描述
 * 将一个字符由小写字母转换为大写字母
 * 你可以假设输入一定在小写字母 a ~ z 之间
 */
public class P145_LowerToUpper {
    public char lowercaseToUppercase(char character) {
        return (char)(int)(character - 32);
    }
}
