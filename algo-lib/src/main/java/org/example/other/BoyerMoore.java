package org.example.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMoore {
    
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
                
                // Сдвигаем так, чтобы следующий суффикс совпал
                if (shift + patternLength < textLength) {
                    shift += patternLength - (goodSuffixTable[0] != -1 ? goodSuffixTable[0] : 1);
                } else {
                    shift++;
                }
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
        int length = pattern.length();
        int[] borderPos = new int[length + 1];  // Граничные позиции
        int[] shift = new int[length + 1];      // Таблица сдвигов
        
        // Инициализация
        for (int i = 0; i <= length; i++) {
            shift[i] = 0;
        }
        
        // Находим границы для каждого суффикса
        borderPos[length] = length + 1;
        int i = length;
        int j = length + 1;
        
        while (i > 0) {
            while (j <= length && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (shift[j] == 0) {
                    shift[j] = j - i;
                }
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }
        
        // Заполняем оставшиеся значения
        j = borderPos[0];
        for (i = 0; i <= length; i++) {
            if (shift[i] == 0) {
                shift[i] = j;
            }
            if (i == j) {
                j = borderPos[j];
            }
        }
        
        return shift;
    }
    
   
    
    public static void main(String[] args) {
        
        // Тест 1
        String text = "HERE IS A SIMPLE EXAMPLE";
        String pattern = "EXAMPLE";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        List<Integer> result = search(text, pattern);
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
        result = search(text, pattern);
        System.out.println("Найден на позициях: " + result);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 3 - образец не найден
        text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        pattern = "JAVA";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = search(text, pattern);
        System.out.println("Результат: " + (result.isEmpty() ? "Не найден" : "Найден на " + result));
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 4 - повторяющиеся символы
        text = "AAAAAAAAAAAAAAAAAAAA";
        pattern = "AAAAA";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = search(text, pattern);
        System.out.println("Найден на позициях: " + result);
        System.out.println("Всего вхождений: " + result.size());
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Тест 5 - демонстрация работы алгоритма
        text = "GCATCGCAGAGAGTATACAGTACG";
        pattern = "GCAGAGAG";
        
        System.out.println("Текст: " + text);
        System.out.println("Образец: " + pattern);
        result = search(text, pattern);
        System.out.println("Найден на позициях: " + result);
        if (!result.isEmpty()) {
            System.out.println("Подтверждение: " + text.substring(result.get(0), 
                                   result.get(0) + pattern.length()));
        }
    }
}