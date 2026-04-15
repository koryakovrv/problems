package org.example.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMoore {
    
    /**
     * Упрощенный алгоритм Бойера-Мура-Хорспула
     * @param text текст, в котором ищем
     * @param pattern образец для поиска
     * @return список индексов начала вхождений
     */
    public static List<Integer> searchHorspool(String text, String pattern) {
        List<Integer> result = new ArrayList<Integer>();
        var shiftTable = buildShiftTable(pattern);
        
        int len = text.length();
        int i = 0;
        
        
        while (i <= len - pattern.length()) {
            int j = pattern.length() - 1;
            while (j >= 0 && text.charAt(i + j) == pattern.charAt(j)) {
                j --;
            }
            
            if (j < 0) {
                result.add(i);
                i ++;
            } else {

                char badChar = text.charAt(i + j);
                int lastPos = shiftTable.getOrDefault(badChar, -1);
                
                int shift = j - lastPos;
                if (shift < 1) shift = 1;
                
                i += shift;
            }
        }
        
        return result;
    }
    
    /**
     * Формируем таблицу смещений 
     * @param pattern
     * @return
     */
    private static Map<Character, Integer> buildShiftTable(String pattern) {
        Map<Character, Integer> result = new HashMap<>();
        
        for (int i = 0; i < pattern.length(); i ++) {
            result.put(pattern.charAt(i), i);
        }
        
        return result;
    }
    
    /**
     * Поиск всех вхождений образца в текст с использованием алгоритма Бойера-Мура
     * @param text текст, в котором ищем
     * @param pattern образец для поиска
     * @return список индексов начала вхождений
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        if (pattern == null || pattern.isEmpty() || text == null || text.isEmpty()) {
            return occurrences;
        }
        
        if (pattern.length() > text.length()) {
            return occurrences;
        }
        
        // Предварительные вычисления
        Map<Character, Integer> badCharTable = buildBadCharTable(pattern);
        int[] goodSuffixTable = buildGoodSuffixTable(pattern);
        
        int patternLength = pattern.length();
        int textLength = text.length();
        
        int shift = 0; // Сдвиг образца относительно текста
        
        while (shift <= textLength - patternLength) {
            int j = patternLength - 1; // Индекс в образце (с конца)
            
            // Сравнение символов справа налево
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }
            
            if (j < 0) {
                // Образец найден
                occurrences.add(shift);
                
                shift += goodSuffixTable[0];
                
            } else {
                // Вычисляем сдвиг по правилу стоп-символов
                char badChar = text.charAt(shift + j);
                int badCharShift = badCharTable.getOrDefault(badChar, -1);
                
                // Сдвиг по стоп-символу
                int badCharRuleShift = j - badCharShift;
                if (badCharRuleShift < 1) {
                    badCharRuleShift = 1;
                }
                
                // Сдвиг по правилу суффикса
                int suffixRuleShift = goodSuffixTable[j + 1];
                
                // Используем максимальный сдвиг
                shift += Math.max(badCharRuleShift, suffixRuleShift);
            }
        }
        
        return occurrences;
    }
    
    /**
     * Построение таблицы стоп-символов
     * @param pattern образец
     * @return map символов с их последней позицией в образце
     */
    private static Map<Character, Integer> buildBadCharTable(String pattern) {
        Map<Character, Integer> table = new HashMap<>();
        int length = pattern.length();
        
        for (int i = 0; i < length; i++) {
            table.put(pattern.charAt(i), i);
        }
        
        return table;
    }
    
    /**
     * Построение таблицы хороших суффиксов
     * @param pattern образец
     * @return массив сдвигов для каждого суффикса
     */
    private static int[] buildGoodSuffixTable(String pattern) {
        int m = pattern.length();
        int[] shift = new int[m + 1];
        int[] borderPos = new int[m + 1];
        
        // Инициализация
        for (int i = 0; i <= m; i++) {
            shift[i] = m; // по умолчанию сдвиг = длина образца
        }
        
        // borderPos[i] - позиция начала границы для суффикса, начинающегося на i
        borderPos[m] = m + 1;
        int i = m;
        int j = m + 1;
        
        // Первый проход: находим границы для каждого суффикса
        while (i > 0) {
            // Ищем границу
            while (j <= m && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (shift[j] == m) {
                    shift[j] = j - i;
                }
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }
        
        // Второй проход: заполняем оставшиеся значения
        j = borderPos[0];
        for (i = 0; i <= m; i++) {
            if (shift[i] == m) {
                shift[i] = j;
            }
            if (i == j) {
                j = borderPos[j];
            }
        }
        
        System.out.println("Результат: " + Arrays.toString(shift));
        return shift;
    }
    
   
    
    public static void main(String[] args) {
        
        // Тест 1
        String text = "HERE IS A SIMPLE EXAMPLE";
        String pattern = "EXAMPLE";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        List<Integer> result = searchHorspool(text, pattern);
        System.out.println("Найден на позициях: " + result);
        if (!result.isEmpty()) {
            System.out.println("Подтверждение: " + text.substring(result.get(0), 
                                   result.get(0) + pattern.length()));
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 2 - множественные вхождения
        text = "ABABDABACDABABCABAB";
        pattern = "ABABCABAB";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = searchHorspool(text, pattern);
        System.out.println("Найден на позициях: " + result);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 3 - образец не найден
        text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        pattern = "JAVA";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = searchHorspool(text, pattern);
        System.out.println("Результат: " + (result.isEmpty() ? "Не найден" : "Найден на " + result));
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 4 - повторяющиеся символы
        text = "AAAAAAAAAAAAAAAAAAAA";
        pattern = "AAAAA";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = searchHorspool(text, pattern);
        System.out.println("Найден на позициях: " + result);
        System.out.println("Всего вхождений: " + result.size());
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 5 - демонстрация работы алгоритма
        text = "GCATCGCAGAGAGTATACAGTACG";
        pattern = "GCAGAGAG";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = searchHorspool(text, pattern);
        System.out.println("Найден на позициях: " + result);
        if (!result.isEmpty()) {
            System.out.println("Подтверждение: " + text.substring(result.get(0), 
                                   result.get(0) + pattern.length()));
        }
    }
}