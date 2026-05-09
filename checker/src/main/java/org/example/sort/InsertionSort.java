package org.example.sort;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class InsertionSort {
    
    static void sort(int[] a) {
        int j;
        
        for (int i = 1; i < a.length; i ++) {
            int t = a[i];
            
            for (j = i; j > 0 && t < a[j - 1]; j --) {
                a[j] = a[j - 1];
            }
            
            a[j] = t;
        }
    }
    
    static void optimizedSort(int[] a) {
        int j;
        
        for (int i = 1; i < a.length; i ++) {
            int t = a[i];
            
            for (j = i; j > 0 && t < a[j - 1]; j --) {
                a[j] = a[j - 1];
            }
            
            a[j] = t;
        }
    }
    
    static int[] createRandom(int n) {
        int[] result = new int[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            result[i] =  r.nextInt(n);
        }
        return result;
    }
    
    static int[] createSorted(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = i;
        }
        return result;
    }
    
    
    public static void main(String[] args) {
        int n = 100000;
        
        int[] a = createRandom(n);
        System.out.println("Array of " + n + " elements.");
        Instant start = Instant.now();
        sort(a);
        Instant finish = Instant.now();
        System.out.println("Insertion sorting time is " + Duration.between(start, finish).toMillis() + " ms");
    } 
}
