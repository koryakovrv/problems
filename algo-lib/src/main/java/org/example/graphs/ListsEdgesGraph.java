package org.example.graphs;

import org.example.list.ArrayLinkedList;
import org.utils.graphs.Pair;

/*
 * Список вершин и список ребер 
 * + Эффективно если требуется полный обход графа, добавление/удаление вершин
 * - O(n) для доступа к вершине 
 */
public class ListsEdgesGraph<T> {
    // связные списки дуг (собственная реализация ArrayLinkedList на смежной памяти)
    ArrayLinkedList<Pair<T, ArrayLinkedList<T>>> edgeLists;
    

    public ListsEdgesGraph() {
        edgeLists = new ArrayLinkedList<>();
    }
    
    public void addV(T v, T[] lVs) {
        ArrayLinkedList<T> edges = new ArrayLinkedList<>();
        for (var lv : lVs) {
            edges.add(lv);
        }
        edgeLists.add(new Pair<>(v, edges));
    }
    
    public static void main(String[] args) {
        ListsEdgesGraph<String> g = new ListsEdgesGraph<>();

        g.addV("V1", new String[] { "V2", "V3", "V4" });
        g.addV("V2", new String[] {});
        g.addV("V3", new String[] { "V1", "V2", "V4" });
        g.addV("V4", new String[] { "V1" });
        
        
        g.edgeLists.forEach(v -> {
            System.out.println("\n" + v.firstElem() + ":");
            v.secondElem().forEach(edge -> System.out.print(edge + " "));
        });
    }
}
