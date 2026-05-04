package org.example.bits.chesse;

import java.util.List;

import org.example.checker.Solution;

public class HorseProblem implements Solution {

    @Override
    public String solve(List<String> args) {
        int pos = Integer.parseInt(args.get(0));
        long horse = 1L << pos;
        
        long notA = 0xFEFEFEFEFEFEFEFEL;
        long notAB = 0xFCFCFCFCFCFCFCFCL;
        long notH = 0x7F7F7F7F7F7F7F7FL;
        long notGH = 0x3F3F3F3F3F3F3F3FL;
        
        long moves = 0;
        
        moves |= (horse & notGH) << 10;   // +2,+1 (вправо-вверх) 
        moves |= (horse & notGH) >>> 6;   // +2,-1 (вправо-вниз) 
        
        moves |= (horse & notH) << 17;    // +1,+2 (вверх-вправо) 
        moves |= (horse & notH) >>> 15;   // +1,-2 (вниз-вправо) 
        
        moves |= (horse & notA) << 15;    // -1,+2 (вверх-влево) 
        moves |= (horse & notA) >>> 17;   // -1,-2 (вниз-влево) 
        moves |= (horse & notAB) << 6;    // -2,+1 (влево-вверх) 
        moves |= (horse & notAB) >>> 10;  // -2,-1 (влево-вниз) 
        
        moves &= ~horse;
        moves &= 0xFFFFFFFFFFFFFFFFL;
        
        int count = Long.bitCount(moves);
        
        return count + "\n" + moves;
    }
}