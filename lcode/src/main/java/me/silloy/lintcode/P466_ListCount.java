package me.silloy.lintcode;

/**
 * 描述
 * 计算链表中有多少个节点.
 * <p>
 * 您在真实的面试中是否遇到过这个题？
 * 样例
 * 给出 1->3->5, 返回 3.
 */

import lombok.Data;

/**
 * Definition for ListNode
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class P466_ListCount {
    /**
     * @param head: the first node of linked list.
     * @return: An integer
     */
    public int countNodes(ListNode head) {
        if (head == null) {
            return 0;
        }
        int count = 1;
        while (head.next != null) {
            head = head.next;
            count++;
        }
        return count;
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }


}
