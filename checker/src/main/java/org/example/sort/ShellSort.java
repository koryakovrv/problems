package org.example.sort;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class ShellSort {
    static long swaps = 0;
    
    static void sort(int[] a) {
        int n = a.length;
        int gap = n / 2;
        
        while (gap > 0) {
            for (int i = gap; i < n; i++) {
                int j = i;
                while (j >= gap && a[j - gap] > a[j]) {
                    swap(a, j, j - gap);
                    j -= gap;
                }
            }
            gap /= 2;
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
        int n = 10_000_000;
        
        int[] a = createRandom(n);
        System.out.println("Array of " + n + " elements.");
        Instant start = Instant.now();
        sort(a);
        Instant finish = Instant.now();
        System.out.println("Shell sorting time is " + Duration.between(start, finish).toMillis() + " ms");
        System.out.println("Swappings is " + swaps);
        
        //System.out.println(Arrays.toString(a));
    } 
}
