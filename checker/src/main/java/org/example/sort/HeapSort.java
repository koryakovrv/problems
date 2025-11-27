package org.example.sort;

import java.util.Arrays;
import java.util.List;

import org.example.checker.Solution;

public class HeapSort implements Solution {

    @Override
    public String solve(List<String> args) {
        Long[] a = new Long[args.size()];
        for (int i = 0; i < a.length; i ++) {
            a[i] = Long.parseLong(args.get(i));
        }
        
        return Arrays.toString(sort(a));
    }
    
    Long[] sort(Long [] a) {
        
        for (int i = a.length / 2 - 1; i >= 0; i --) {
            heapify(a, i, a.length);
        }
        
        for (int i = a.length - 1; i >= 0; i --) {
            swap(a, 0, i);
            heapify(a, 0, i);
        }
        return a;
    }
    
    void heapify(Long [] a, int root, int len) {
        int left = root * 2 + 1;
        int right = left + 1;
        
        int t = root;
        if (left < len && a[root] < a[left]) t = left;
        if (right < len && a[t] < a[right]) t = right;
        
        if (t == root)
            return;
        
        swap(a, root, t);
        heapify(a, t, len);
    }
    
    void swap(Long [] a, int i, int j) {
        var t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
