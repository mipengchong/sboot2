package me.silloy.lintcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class P2_TerminalZeroTest {

    @Test
    void trailingZeros() {
        P2_TerminalZero terminalZero = new P2_TerminalZero();
        Assertions.assertEquals(2, terminalZero.trailingZeros(11));
    }
}