package org.example.lesson3;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import org.example.checker.Solution;

public class Pow implements Solution {
    
    @Override
    public String solve(List<String> args) {
        Double x = Double.parseDouble(args.get(0));
        Long k = Long.parseLong(args.get(1));
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(11);
        nf.setMinimumFractionDigits(1);
        nf.setGroupingUsed(false);
        return nf.format(bin_pow(x, k));
    }

    
    Double pow1(Double x, Long k) {
        Double result = x;
        
        if (k == 0) return 1.0;
        
        for (int i = 1; i < k; i ++) {
            result *= x;
        }
        
        return result;
    }
    
    Double pow2(Double x, Long k) {
        if (k == 0) return 1.0;
        if (k == 1) return x;
        
        // Базовые случаи для улучшения точности
        if (x == 0.0) return 0.0;
        if (x == 1.0) return 1.0;
        
        Long halfK = k / 2;
        Long remainder = k % 2;
        
        Double halfPower = pow2(x, halfK);
        
        if (remainder == 0) {
            return halfPower * halfPower;
        } else {
            return x * halfPower * halfPower;
        }
    }
    
    BigDecimal powBigDecimal(BigDecimal x, Long k) {
        if (k == 0) return BigDecimal.ONE;
        
        if (k % 2 == 0) {
            BigDecimal result = powBigDecimal(x, k / 2);
            return result.multiply(result);
        } else {
            BigDecimal result = powBigDecimal(x, (k - 1) / 2);
            return x.multiply(result).multiply(result);
        }
    }
    
    double bin_pow(double a, Long n)
    {
        if (n == 0) return 1.0;
        long mask = 0x80000000L;
        while ((n & mask) == 0)
            mask >>= 1;
        double p = 1.0;
        while(mask > 0)
        {
            int s = (n & mask) > 0 ? 1 : 0;
            if (s == 0)
                p = p * p;
            else
                p = p * p * a;// 10^4:  p = 10*10, p = p*p
            mask = mask >> 1;
        }
        return p;
    }
}
