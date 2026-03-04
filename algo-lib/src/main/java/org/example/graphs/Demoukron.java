package org.example.graphs;

import java.util.Arrays;

public class Demoukron {
    // структура с оглавлением
    CSRGraph<String> g = new CSRGraph<>(new String[]{"a", "b", "c", "d", "e", "f"},// вершины
            new int[][] {{0, 3}, {0, 1}, // дуги
                         {1, 5},
                         {2, 1},
                         {3, 5},
                         {5, 4}});
    
    int[] result = new int[g.vs.length];
    int resultIdx = 0;
    
    int[] queue = new int[g.vs.length];
    int head = 0;
    int tail = head;
    
    public int[] solve() {
        int[] weight = new int[g.vs.length];
        boolean[] visited = new boolean[g.vs.length];
        
        // Вычисляем полустепени захода
        for (int i = 0; i < g.vs.length; i ++) {
            for (int j = g.head[i]; j < g.head[i + 1]; j ++) {
                weight[g.list[j]] ++;
            }
        }
        
        // инициализируем очередь вершинами первого слоя
        for (int i = 0; i < g.vs.length; i ++) {
            if (weight[i] == 0) {
                queue[tail ++] = i;
            }
        }
        
        while (head <= tail) {
            int index = queue[head ++];
            if (visited[index]) continue;
            visited[index] = true;
            head = head == queue.length ? 0 : head;
            
            result[resultIdx ++] = index;
            
            for (int j = g.head[index]; j < g.head[index + 1]; j ++) {
                if (-- weight[g.list[j]] == 0) {
                    queue[tail ++] = g.list[j];
                    tail = tail == queue.length ? 0 : tail;
                }
            }
        }
        
        for (int i = 0; i < g.vs.length; i ++) {
            if (!visited[i]) {
                result[resultIdx ++] = i;
            }
        }
        
        if (resultIdx != g.vs.length) {
            throw new IllegalStateException("Граф содержит цикл");
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        
        System.out.println("\nТест 1: Ациклический граф");
        Demoukron demoukron = new Demoukron();

        int[] order = demoukron.solve();

        System.out.println("Порядок вершин (индексы): " + Arrays.toString(order));

        System.out.print("Порядок вершин (метки): ");
        for (int i = 0; i < order.length; i++) {
            System.out.print(demoukron.g.vs[order[i]] + " ");
        }
        System.out.println();

        // Проверка корректности (все ли вершины обработаны)
        System.out.println("Обработано вершин: " + order.length + " из " + demoukron.g.vs.length);

    }
}
