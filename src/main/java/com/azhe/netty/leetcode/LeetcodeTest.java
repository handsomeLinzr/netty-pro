package com.azhe.netty.leetcode;

public class LeetcodeTest {

    public static void main(String[] args) {
        LeetcodeTest leetcodeTest = new LeetcodeTest();
//        int[] arr = {1,3,6,4,2,1,0,2};
//        leetcodeTest.insertSort(arr);
//        leetcodeTest.sout(arr);
        int i = leetcodeTest.binSearch(new int[]{1, 2, 4, 5, 7, 8, 9, 12, 15, 20}, 20);
        System.out.println(i);
    }

    // 选择排序
    public void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                min = arr[j] > arr[min]? min:j;
            }
            swap(arr, i, min);
        }
    }
    // 冒泡排序
    public void maopaoSort(int[] arr){
        for (int i = arr.length - 1; i>0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j+1]) {
                    swap(arr, j, j+1);
                }
            }
        }
    }
    // 插入排序
    public void insertSort(int[] arr){
        // 从1开始，因为 0~0 肯定有序
        for (int i = 1; i < arr.length; i++) {
            // j为i的前一位，j不能越界且大于i时，交换
            for (int j = i-1; j >= 0 && arr[j] > arr[j+1]; j--) {
                swap(arr, j, j+1);
            }
        }
    }
    // 二分查找
    public int binSearch(int[] arr, int num) {
        // 判断不为空
        if (arr.length == 0) return -1;

        // 首位置和最后一个位置
        int left = 0, right = arr.length - 1;
        int mid;
        // 左右相邻时结束，彻底解决空循环问题
        while (left < right-1) {
            // 不需要加1
            mid = left + ((right - left ) >>>1);
            // 最后很有可能在左边
            if (arr[mid] <= num) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return arr[left] == num? left:arr[right] == num?right:-1;
    }

    private void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }
    private void sout(int[] arr){
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

}
