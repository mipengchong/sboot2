package me.silloy.lintcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class P366_FibonacciArraysTest {

    @Test
    void fibonacci() {
        P366_FibonacciArrays fibonacciArrays = new P366_FibonacciArrays();
        assertEquals(102334155, fibonacciArrays.fibonacci(41));
    }
}