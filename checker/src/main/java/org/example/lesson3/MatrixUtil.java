package org.example.lesson3;

import java.math.BigDecimal;

public class MatrixUtil {
    
    private MatrixUtil() {
    }
    
    
    /* 
     * возведение матрицы в степень с исп. бинарного алгоритма
     * */
    public static BigDecimal[][] pow(BigDecimal[][] a, long degree) {
        
        if (degree == 0) {
            BigDecimal[][] res = new BigDecimal[a.length][a[0].length];
            
            if (a.length != a[0].length) return null;
            
            for (int i = 0; i < a.length; i ++) {
                for (int j = 0; j < a[0].length; j ++) {
                    res[i][j] = BigDecimal.ZERO;
                    if (i == j) res[i][j] = BigDecimal.ONE;
                }
            }
            
            return res;
        }
        
        BigDecimal[][] result = a;
        BigDecimal[][] temp = a;
        
        while (degree > 0) {
            if ((degree & 1) == 1) {
                result = mul(result, temp);
            }
            temp = mul(temp, temp);
            degree >>= 1;
        }
        
        return result;
    }
    
    public static BigDecimal[][] mul(BigDecimal[][] a, BigDecimal[][] b) {
        BigDecimal[][] result = null;

        if (a == null || b == null || b.length != a[0].length 
                || a.length == 0 || b.length == 0) {
            return null;
        }

        result = new BigDecimal[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int k = 0; k < result[0].length; k ++) {
                result[i][k] = BigDecimal.ZERO;
            }
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                BigDecimal aij = a[i][j]; // для уменьшения обращений к памяти
                for (int k = 0; k < result[0].length; k ++) {
                    result[i][k] = result[i][k].add(aij.multiply(b[j][k]));
                }
            }
        }

        return result;
    }
    
    
}
