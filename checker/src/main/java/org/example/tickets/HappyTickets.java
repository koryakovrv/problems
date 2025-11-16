package org.example.tickets;

import java.util.Arrays;
import java.util.List;

import org.example.checker.Solution;

public class HappyTickets implements Solution {
    
    
    @Override
    public String solve(List<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        int n = Integer.parseInt(args.get(0));
        
        return Long.toString(solve(n));
    }
    
//    private long solve2(int n) {
//        long result = 0;
//        
//        while (n > 0) {
//            long currN = 2;
//            long currMul = 1;
//            // C(n+k-1, k-1)^2 Идея решения
//            // где n = N*9, k = N
//            // результат сокращения дроби (n+k-1)!/(n+k-1 - k-1)! считаем полученное произведение
//            // одновременно по возможности делим на множители (k-1)! 
//            for (int i = n*9+1; i <= n*9+(n-1); i ++) {
//                currMul *= i;
//                currMul = (currN < n && currMul % currN == 0) ? currMul / currN ++ : currMul;
//            }
//            
//            while (currN < n) {
//                currMul /= currN ++;
//            }
//            
//            result += currMul*currMul;
//            n --;
//        }
//        return result;
//    }
    
    private long solve(int n) {
        long[] dp = new long[n * 9 + 1];
        Arrays.fill(dp, 0, 10, 1);  // для 1 цифры
        
        for (int digitCount = 2; digitCount <= n; digitCount ++) {
            long[] newDp = new long[n * 9 + 1];
            
            for (int sum = 0; sum <= (digitCount - 1) * 9; sum ++) {
                for (int digit = 0; digit <= 9; digit++) {
                    newDp[sum + digit] += dp[sum];
                }
            }
            dp = newDp;
        }
        
        long result = 0;
        for (long count : dp) {
            result += count * count;
        }
        
        return result;
    }
}
