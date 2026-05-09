package org.example.sort;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class BubbleSort {
    static long swaps = 0;
    
    static void sort(int[] a) {
        for (int i = 0; i < a.length - 1; i ++) {
            for (int j = i + 1; j < a.length; j ++) {
                if (a[i] > a[j]) {
                    swap(a, i, j);
                }
            }
        }
    }
    
    // Сортировка пузырьком оптимизированная методом двух указателей
    static void sortOptimized(int[] a) {
        int left = 0;
        int right = a.length;
        boolean flag = true;
        
        while (left < right && flag) {
            flag = false;
            for (int i = left + 1; i < right; i ++) {
                if (a[i] < a[i - 1]) {
                    swap(a, i, i - 1);
                    flag = true;
                }
            }

            right --;
            
            for (int i = right - 1; i > left; i --) {
                if (a[i] < a[i - 1]) {
                    swap(a, i, i - 1);
                    flag = true;
                }
            }
            
            left ++;
        }
    }
    
    static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        swaps ++;
    }
    
    static int[] createRandom(int n) {
        swaps = 0;
        int[] result = new int[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            result[i] =  r.nextInt(n);
        }
        return result;
    }
    
    static int[] createSorted(int n) {
        swaps = 0;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = i;
        }
        return result;
    }
    
    
    public static void main(String[] args) {
        int n = 50_000;
        
        int[] a = createRandom(n);
        System.out.println("Array of " + n + " elements.");
        Instant start = Instant.now();
        sort(a);
        Instant finish = Instant.now();
        System.out.println("Bubble sorting time is " + Duration.between(start, finish).toMillis() + " ms");
        System.out.println("Swappings is " + swaps);
        
        
        a = createRandom(n);
        System.out.println("Array of " + n + " elements.");
        start = Instant.now();
        sortOptimized(a);
        finish = Instant.now();
        System.out.println("Optimized bubble time is " + Duration.between(start, finish).toMillis() + " ms");
        System.out.println("Swappings is " + swaps);
        //System.out.println(Arrays.toString(a));
    } 
}
