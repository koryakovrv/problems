package org.example.checker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Система тестирования
 */
public class Checker {
    
    private Checker() {
    }
    
    public static void check(Solution solution, String inDir, String outDir) throws IOException {
        int iter = 0;
        
        while (true) {
            var inRes = Checker.class.getResource(inDir + "test." + iter + ".in");
            var outRes = Checker.class.getResource(outDir + "test." + iter + ".out");
            
            if (inRes == null || outRes == null) {
                return;
            }
            
            var fileIn = Paths.get(inRes.getPath());
            var fileOut = Paths.get(outRes.getPath());
            
            List<String> input = Files.readAllLines(fileIn);
            String expected = StringUtils.trim(Files.readString(fileOut));
            String answer = solution.solve(input);
            
            if (expected != null && expected.equals(answer)) {
                System.out.printf("Тест %1$d OK: %2$s %n", iter, answer);
            } else {
                System.out.printf("Тест %1$d ошибка: %2$s ожидалось: %3$s%n", iter, answer, expected);
            }
            
            iter ++;
        }
    }
     
}
