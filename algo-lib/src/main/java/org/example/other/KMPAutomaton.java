package org.example.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация КМП с использованием конечного автомата
 */
class KMPAutomaton {
    private int[][] transition;
    private String pattern;
    private int m;
    
    public KMPAutomaton(String pattern) {
        this.pattern = pattern;
        this.m = pattern.length();
        buildAutomaton();
    }
    
    private void buildAutomaton() {
        int[] pi = buildPrefixFunction(pattern);
        transition = new int[m + 1][256]; // 256 - размер ASCII алфавита
        
        for (int state = 0; state <= m; state++) {
            for (char c = 0; c < 256; c ++) {
                if (state < m && c == pattern.charAt(state)) {
                    transition[state][c] = state + 1;
                } else if (state > 0) {
                    transition[state][c] = transition[pi[state - 1]][c];
                } else {
                    transition[state][c] = 0;
                }
            }
        }
    }
    
    private int[] buildPrefixFunction(String pattern) {
        int[] pi = new int[m];
        int k = 0;
        
        for (int i = 1; i < m; i++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = pi[k - 1];
            }
            if (pattern.charAt(k) == pattern.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }
    
    public List<Integer> search(String text) {
        List<Integer> occurrences = new ArrayList<>();
        int state = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            state = transition[state][c];
            
            if (state == m) {
                occurrences.add(i - m + 1);
                //state = transition[state][c]; // Продолжаем поиск
            }
        }
        
        return occurrences;
    }
}