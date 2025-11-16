package org.example.lesson3;

import java.math.BigDecimal;
import java.util.List;

import org.example.checker.Solution;

public class Fib implements Solution {

    @Override
    public String solve(List<String> args) {
        var res = fib2(Long.parseLong(args.get(0)));
        return String.valueOf(res);
    }
    
    private long fib1(long n) {
        if (n == 1 || n == 2 ) return 1;
        if (n == 0) return 0;
        
        return fib1(n - 1) + fib1(n - 2);
    }
    
    private BigDecimal fib2(long n) {
        BigDecimal result = new BigDecimal(0);
        
        if (n == 1 || n == 2) return result.add(BigDecimal.ONE);
        if (n == 0) return new BigDecimal(0);
        
        BigDecimal n1 = new BigDecimal(1), n2 = new BigDecimal(1);
        
        for (long i = 2; i < n; i ++) {
            result = n1.add(n2);
            n1 = n2;
            n2 = result;
        }
        
        return result;
    }
}
