package me.silloy.lintcode;

/**
 * 描述
 * 给一组整数，按照升序排序，使用选择排序，冒泡排序，插入排序或者任何 O(n2) 的排序算法。
 *
 * 您在真实的面试中是否遇到过这个题？
 * 样例
 * 对于数组 [3, 2, 1, 4, 5], 排序后为：[1, 2, 3, 4, 5]。
 *
 */
public class P463_Sorting {

    public void sortIntegers(int[] A) {
        if (A == null || A.length <= 1) {
            return;
        }
        for (int i = 0; i < A.length - 1; i++) {
            for (int j = 0; j < A.length - i -1; j++) {
                if (A[j] > A[j + 1]) {
                    swap(A, j,  j + 1);
                }
            }
        }
    }


    public void swap(int[] A,int i,int j){
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }

}
