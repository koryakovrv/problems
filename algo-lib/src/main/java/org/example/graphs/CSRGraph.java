package org.example.graphs;

import java.util.Arrays;
import java.util.Objects;

/*
 * Структура графа с оглавлением
 * CSR - Compressed Sparse Row
 */
public class CSRGraph<T> {
    
    // указатели на начало списка смежных вершин для vs[i]
    int[] head;
    
    // метки вершин
    T[] vs;
    
    // структура смежных вершин
    int[] list;
    
    // веса дуг
    int[] weight;
    
    // ребра с весом
    public record Edge(int source, int target, int weight) {};
    
    
    /**
     * 
     * @param v - вершины
     * @param edges - ребера (индексы от 0 до v.length - 1)
     */
    public CSRGraph(T[] v, Edge[] edges) {
        Objects.requireNonNull(v, "null_v");
        Objects.requireNonNull(edges, "null_e");
        
        this.vs = Arrays.copyOf(v, v.length);
        head = new int[v.length + 1];
        list = new int[edges.length];
        weight = new int[edges.length];
        
        for (int i = 0; i < edges.length; i ++) {
            head[edges[i].source + 1] ++;
        }
        
        for (int i = 1; i < head.length; i ++) {
            head[i] += head[i - 1];
        }
        
        for (int i = 0; i < edges.length; i ++) {
            list[i] = edges[i].target();
            weight[i] = edges[i].weight();
        }
    }
    
    public static void main(String[] args) {
        
        CSRGraph<Character> g = new CSRGraph<>(new Character[]{'a', 'b', 'c', 'd'},
                new Edge[] {new Edge(0, 1, 2), new Edge(0, 2, 4), new Edge(0, 3, 5),
                            new Edge(1, 2, 1), new Edge(1, 3, 2),
                            new Edge(2, 0, 4), new Edge(2, 3, 6)});

        for (int i = 0; i < g.vs.length; i ++) {
            System.out.println("Вершины смежные с " + g.vs[i]);
            
            for (int j = g.head[i]; j < g.head[i + 1]; j ++) {
                System.out.println(g.list[j] + " - " + g.vs[g.list[j]]);
            }
        }
    }
}
