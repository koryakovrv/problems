package org.example.graphs;

import java.util.Arrays;
import java.util.LinkedList;

import org.example.graphs.CSRGraph.Edge;

public class DijkstraAlgorithm {
    
    /**
     * Реализация алгоритма Дейкстры с использованием структуры графа с оглавлением 
     * (собственная реализация в {@link org.example.graph.CSRGraph})                 
     * @param g {@link org.example.graph.CSRGraph}
     * @param source начальная вершина
     * @return массив кратчайших расстояний от source до всех вершин
     */
    public static int[] dijkstraCSR(CSRGraph<Integer> g, int source) {
        int n = g.vs.length;
        int[] dist = new int [n];
        boolean[] visited = new boolean [n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        for (int i = 0; i < g.vs.length; i ++) {
            
            int minDistV = getMinDistV(dist, visited);
            
            if (dist[minDistV] == Integer.MAX_VALUE) break;
            
            visited[minDistV] = true;
            
            for (int j = g.head[minDistV]; j < g.head[minDistV + 1]; j ++) {
                int newDist = dist[minDistV] + g.weight[j];
                if (!visited[g.list[j]] && newDist < dist[g.list[j]]) {
                    dist[g.list[j]] = newDist;
                }
            }
        }
        
        return dist;
    }
    
    private static int getMinDistV(int[] dist, boolean[] visited) {
        int minDist = Integer.MAX_VALUE;
        int v = 0;
        
        for (int i = 0; i < dist.length; i ++) {
            if (!visited[i] && minDist > dist[i]) {
                minDist = dist[i];
                v = i;
            }
        }
        
        return v;
    }
        
    /**
     * Реализация алгоритма Дейкстры с использованием матрицы смежности
     * @param graph матрица смежности (graph[i][j] - вес ребра i->j, 0 если нет ребра)
     * @param source начальная вершина
     * @param target конечная вершина
     * @return массив кратчайшего пути из source в target
     */
    public static Integer[] dijkstraMatrix(int[][] graph, int source, int target) {
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
        
        // формируем путь 
        int curr = target;
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(target);
        while (curr != source) {
            for (int i = 0; i < n; i ++) {
                if  (graph[i][curr] != 0 && dist[i] == dist[curr] - graph[i][curr]) {
                    stack.push(i);
                    curr = i;
                    break;
                }
            }
        }
        
        return stack.toArray(new Integer[]{});
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
        
        Integer[] path = dijkstraMatrix(matrixGraph, 0, 6);
        System.out.println("Кратчайший путь от вершины 0 до 6:");
        for (int i = 0; i < path.length; i ++) {
            System.out.println(path[i]);
        }
        
        // Пример 2: Граф, представленный структурой с оглавлением

        CSRGraph<Integer> g = new CSRGraph<>(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8},
                new Edge[] {new Edge(0, 1, 4), new Edge(0, 7, 8),
                            new Edge(1, 0, 4), new Edge(1, 2, 8), new Edge(1, 7, 11),
                            new Edge(2, 1, 8), new Edge(2, 3, 7), new Edge(2, 5, 4), new Edge(2, 8, 2),
                            new Edge(3, 2, 7), new Edge(3, 4, 9), new Edge(3, 5, 14),
                            new Edge(4, 3, 9), new Edge(3, 5, 10),
                            new Edge(5, 2, 4), new Edge(5, 3, 14), new Edge(5, 4, 10), new Edge(5, 6, 2),
                            new Edge(6, 5, 2), new Edge(6, 7, 1), new Edge(6, 8, 6),
                            new Edge(7, 0, 8), new Edge(7, 1, 11), new Edge(7, 6, 1), new Edge(7, 8, 7),
                            new Edge(8, 2, 2), new Edge(8, 6, 6), new Edge(8, 7, 7)});
        

        int[] dist = dijkstraCSR(g, 0);
        System.out.println("Кратчайшие расстояния от вершины 0 до всех остальных:");
        for (int i = 0; i < dist.length; i ++) {
            System.out.println(dist[i]);
        }
    }
}