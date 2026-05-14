package org.example.dp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MaxtRectangleAreaTest {
    @Test
    void testEmptyField() {
        // Поле 3x3 без препятствий
        int N = 3;
        int M = 3;
        int[][] obstacles = {};
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(9, result, "Весь участок свободен, максимальная площадь = 9");
    }
    
    @Test
    void testWithObstacles() {
        // Поле 4x5 с несколькими препятствиями
        int N = 4;
        int M = 5;
        int[][] obstacles = {
            {0, 1},  // Препятствие в (0,1)
            {2, 3},  // Препятствие в (2,3)
            {1, 4},  // Препятствие в (1,4)
            {3, 0}   // Препятствие в (3,0)
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(6, result, "Максимальный прямоугольник должен иметь площадь 6");
    }
    
    @Test
    void testAllObstacles() {
        // Все клетки заняты
        int N = 2;
        int M = 2;
        int[][] obstacles = {
            {0, 0}, {0, 1}, {1, 0}, {1, 1}
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(0, result, "Нет свободных клеток, площадь = 0");
    }
    
    @Test
    void testSingleFreeCell() {
        // Только одна свободная клетка
        int N = 3;
        int M = 3;
        int[][] obstacles = {
            {0, 0}, {0, 1}, {0, 2},
            {1, 0}, {1, 2},
            {2, 0}, {2, 1}, {2, 2}
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(1, result, "Только одна свободная клетка");
    }
    
    @Test
    void testLargeRectangle() {
        // Прямоугольник из свободных клеток в центре
        int N = 5;
        int M = 5;
        int[][] obstacles = {
            {0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4},  // Верхняя строка занята
            {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4},  // Нижняя строка занята
            {1, 0}, {2, 0}, {3, 0},                  // Левый столбец занят
            {1, 4}, {2, 4}, {3, 4}                   // Правый столбец занят
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(9, result, "Свободный прямоугольник 3x3 в центре");
    }
    
    @Test
    void testVerticalStrip() {
        // Вертикальная полоса свободных клеток
        int N = 5;
        int M = 3;
        int[][] obstacles = {
            {0, 0}, {0, 2},
            {1, 0}, {1, 2},
            {2, 0}, {2, 2},
            {3, 0}, {3, 2},
            {4, 0}, {4, 2}
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(5, result, "Вертикальная полоса 5x1");
    }
    
    @Test
    void testHorizontalStrip() {
        // Горизонтальная полоса свободных клеток
        int N = 3;
        int M = 5;
        int[][] obstacles = {
            {0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4},  // Верхняя строка занята
            {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}   // Нижняя строка занята
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(5, result, "Горизонтальная полоса 1x5 в средней строке");
    }
    
    @Test
    void testEdgeCaseN1() {
        // Поле 1x5 с одним препятствием
        int N = 1;
        int M = 5;
        int[][] obstacles = {
            {0, 2}
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(2, result, "Максимальный непрерывный отрезок из 2 свободных клеток");
    }
    
    @Test
    void testEdgeCaseM1() {
        // Поле 5x1 с одним препятствием
        int N = 5;
        int M = 1;
        int[][] obstacles = {
            {2, 0}
        };
        
        int result = MaxRectangleArea.findMaxRectangleArea(N, M, obstacles);
        assertEquals(2, result, "Максимальный непрерывный отрезок из 2 свободных клеток");
    }
}
