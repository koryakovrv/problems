package org.example.sort;

import java.util.Arrays;
import java.util.List;

import org.example.checker.Solution;

public class SelectionSort implements Solution {
    @Override
    public String solve(List<String> args) {
        Long[] a = new Long[args.size()];
        for (int i = 0; i < a.length; i ++) {
            a[i] = Long.parseLong(args.get(i));
        }
        
        return Arrays.toString(sort(a));
    }

    Long[] sort(Long[] a) {
        for (int i = 0; i < a.length - 1; i ++) {
            var min = i;
            
            for (int j = i + 1; j < a.length; j ++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            swap(a, i,min);
        }
        
        return a;
    }
    
    void swap(Long [] a, int i, int j) {
        var t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
