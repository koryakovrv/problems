package org.example.lesson3;

import java.util.ArrayList;
import java.util.List;

import org.example.checker.Solution;

public class Primes implements Solution {

    @Override
    public String solve(List<String> args) {
        return String.valueOf(linearSieveBitOptimized(Integer.parseInt(args.get(0))));
        
    }

    
    // O(n)
    public int linearSieve(int n) {
        List<Integer> primes = new ArrayList<>();
        byte[] lp = new byte[n + 1]; // lp[i] - минимальный простой делитель i
        
        for (int i = 2; i <= n; i ++) {
            if (lp[i] == 0) {
                lp[i] = 1;
                primes.add(i);
            }
            
            // Ключевой момент: умножаем только на подходящие простые
            for (int j = 0; j < primes.size(); j++) {
                int p = primes.get(j);
                if (p > i || p * i > n) break;
                lp[p * i] = 1;  // Каждое составное число помечается ОДИН раз
            }
        }
        return primes.size();
    }
    
    // O(n) с оптимизацией памяти
    public int linearSieveBitOptimized(int n) {
        if (n < 2) return 0;
        
        List<Integer> primes = new ArrayList<>();
        int[] isPrime = new int[(2*n + 31) / 32]; // битовый массив
        
        for (int i = 2; i <= n; i++) {
            int intPos = i / 32;
            int bitPos = i % 32;
            int mask = 1 << bitPos;
            
            if ((isPrime[intPos] & mask) == 0) {
                primes.add(i);
            }
            
            for (int j = 0; j < primes.size(); j++) {
                int p = primes.get(j);
                if (p * i > n) break;
                
                int composite = p * i;
                int compIntPos = composite / 32;
                int compBitPos = composite % 32;
                int compMask = 1 << compBitPos;
                
                isPrime[compIntPos] |= compMask;
                
                if (i % p == 0) break;
            }
        }
        return primes.size();
    }
    
}
