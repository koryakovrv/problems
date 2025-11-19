package org.example.lesson3;

import java.util.ArrayList;
import java.util.List;

import org.example.checker.Solution;

public class Primes implements Solution {

    @Override
    public String solve(List<String> args) {
        return null;
        
    }
 
    public List<Integer> linearSieve(int n) {
        List<Integer> primes = new ArrayList<>();
        int[] lp = new int[n + 1]; // lp[i] - минимальный простой делитель i
        
        for (int i = 2; i <= n; i++) {
            if (lp[i] == 0) {
                lp[i] = i;
                primes.add(i);
            }
            
            // Ключевой момент: умножаем только на подходящие простые
            for (int j = 0; j < primes.size(); j++) {
                int p = primes.get(j);
                if (p > lp[i] || p * i > n) break;
                lp[p * i] = p;  // Каждое составное число помечается ОДИН раз
            }
        }
        return primes;
    }

}
