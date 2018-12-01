package me.silloy.lintcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class P37_ReverseNumTest {

    @Test
    void reverseInteger() {
        P37_ReverseNum reverseNum = new P37_ReverseNum();
        assertEquals(789, reverseNum.reverseInteger(987));
    }
}