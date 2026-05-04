package org.example.bits.chesse;

import java.util.List;

import org.example.checker.Solution;

public class KingProblem implements Solution {

    @Override
    public String solve(List<String> args) {
        int pos = Integer.parseInt(args.get(0)); // 0..63
        
        // 1 бит в позиции короля
        long king = 1L << pos;
        
        // Маски для крайних вертикалей
        long notA = 0xFEFEFEFEFEFEFEFEL;  // ~0x0101010101010101
        long notH = 0x7F7F7F7F7F7F7F7FL;  // ~0x8080808080808080
        
        // Все 8 направлений ходов короля
        long moves = 0;
        moves |= (king & notA) >>> 1;   // влево
        moves |= (king & notH) << 1;    // вправо
        moves |= king >>> 8;            // вверх
        moves |= king << 8;             // вниз
        moves |= (king & notA) >>> 9;   // вверх-влево
        moves |= (king & notH) >>> 7;   // вверх-вправо
        moves |= (king & notA) << 7;    // вниз-влево
        moves |= (king & notH) << 9;    // вниз-вправо
        
        // Количество битов в moves
        int count = Long.bitCount(moves);
        
        return count + "\n" + moves;
    }
}
