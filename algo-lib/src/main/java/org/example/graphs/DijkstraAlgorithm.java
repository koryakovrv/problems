package org.example.graphs;

import java.util.Arrays;

public class DijkstraAlgorithm {
    
    // Класс для представления ребра графа
    static class Edge {
        int target;
        int weight;
        
        Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }
        
    /**
     * Реализация алгоритма Дейкстры с использованием матрицы смежности
     * @param graph матрица смежности (graph[i][j] - вес ребра i->j, 0 если нет ребра)
     * @param source начальная вершина
     * @return массив кратчайших расстояний от source до всех вершин
     */
    public static int[] dijkstraMatrix(int[][] graph, int source) {
        int n = graph.length;
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        for (int i = 0; i < n - 1; i++) {
            // Находим вершину с минимальным расстоянием среди непосещенных
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            
            for (int v = 0; v < n; v++) {
                if (!visited[v] && dist[v] < minDist) {
                    minDist = dist[v];
                    u = v;
                }
            }
            
            if (u == -1) break; // Нет доступных вершин
            visited[u] = true;
            
            // Обновляем расстояния до соседей
            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE) {
                    int newDist = dist[u] + graph[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                    }
                }
            }
        }
        
        return dist;
    }
    
    // Пример использования
    public static void main(String[] args) {
        // Пример 1: Граф, представленный матрицей смежности
        System.out.println("=== Пример 1: Матрица смежности ===");
        int[][] matrixGraph = {
            {0, 4, 0, 0, 0, 0, 0, 8, 0},
            {4, 0, 8, 0, 0, 0, 0, 11, 0},
            {0, 8, 0, 7, 0, 4, 0, 0, 2},
            {0, 0, 7, 0, 9, 14, 0, 0, 0},
            {0, 0, 0, 9, 0, 10, 0, 0, 0},
            {0, 0, 4, 14, 10, 0, 2, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 1, 6},
            {8, 11, 0, 0, 0, 0, 1, 0, 7},
            {0, 0, 2, 0, 0, 0, 6, 7, 0}
        };
        
        int[] distances1 = dijkstraMatrix(matrixGraph, 0);
        System.out.println("Кратчайшие расстояния от вершины 0:");
        for (int i = 0; i < distances1.length; i++) {
            System.out.println("до вершины " + i + ": " + distances1[i]);
        }
    }
}