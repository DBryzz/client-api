//package Maths;

import java.util.Scanner;

public class FindMaxRecursion {

private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int[] array = new int[7];
        System.out.println("Enter 7 numbers");
        for(int i=0; i<array.length; i++) {
        	array[i] = scanner.nextInt();
        }
        
        int low = 0;
        int high = array.length - 1;

        System.out.println("max value is " + max(array, low, high));
    }

    /**
     * Get max of array using divide and conquer algorithm
     *
     * @param array contains elements
     * @param low   the index of the first element
     * @param high  the index of the last element
     * @return max of {@code array}
     */
    public static int max(int[] array, int low, int high) {
        if (low == high) {
            return array[low]; //or array[high]
        }

        int mid = (low + high) >>> 1;

        int leftMax = max(array, low, mid); //get max in [low, mid]
        int rightMax = max(array, mid + 1, high); //get max in [mid+1, high]

        return leftMax >= rightMax ? leftMax : rightMax;
    }
}
