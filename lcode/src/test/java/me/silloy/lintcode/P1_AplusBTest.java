package me.silloy.lintcode;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class P1_AplusBTest {

    @Test
    public void aplusb() {
        P1_AplusB aplusB = new P1_AplusB();
        assertEquals(54,  aplusB.aplusb(23,31));
    }
}
