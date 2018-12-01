package me.silloy.lintcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class P466_ListCountTest {

    @Test
    void countNodes() {
        P466_ListCount listCount = new P466_ListCount();
        P466_ListCount.ListNode node1 = new P466_ListCount.ListNode(4);
        P466_ListCount.ListNode node2 = new P466_ListCount.ListNode(3);
        P466_ListCount.ListNode node3 = new P466_ListCount.ListNode(2);
        P466_ListCount.ListNode node4 = new P466_ListCount.ListNode(1);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        assertEquals(4, listCount.countNodes(node1));
    }
}