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
    
    // вершины
    T[] vs;
    
    // структура смежных вершин
    int[] list;

    
    /**
     * 
     * @param v - вершины
     * @param edges - ребера (индексы от 0 до v.length - 1)
     */
    public CSRGraph(T[] v, int [][] edges) {
        Objects.requireNonNull(v, "null_v");
        Objects.requireNonNull(edges, "null_e");
        
        this.vs = Arrays.copyOf(v, v.length);
        head = new int[v.length + 1];
        list = new int[edges.length];
        
        for (int i = 0; i < edges.length; i ++) {
            head[edges[i][0] + 1] ++;
        }
        
        for (int i = 1; i < head.length; i ++) {
            head[i] += head[i - 1];
        }
        
        for (int i = 0; i < edges.length; i ++) {
            list[i] = edges[i][1];
        }
    }
    
    public static void main(String[] args) {
        
        CSRGraph<String> g = new CSRGraph<>(new String[]{"a", "b", "c", "d"},
                new int[][] {{0, 1}, {0, 2}, {0, 3},
                             {1, 2}, {1, 3},
                             {2, 0}, {2, 3}});

        for (int i = 0; i < g.vs.length; i ++) {
            System.out.println("Вершины смежные с " + g.vs[i]);
            
            for (int j = g.head[i]; j <  g.head[i + 1]; j ++) {
                System.out.println(g.list[j]);
            }
        }
    }
}
