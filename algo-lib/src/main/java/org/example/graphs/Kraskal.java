package org.example.graphs;

import org.example.graphs.CSRGraph.Edge;
import org.example.hashtable.OpenAddressingHashTable;
import org.example.list.ArrayLinkedList;


public class Kraskal {
    // собственная реализация хэш-таблицы с квадратичным пробингом
    // для системы непересекающихся множеств Union-Find
    private OpenAddressingHashTable<Integer, Integer> parent = 
            new OpenAddressingHashTable<>();
    ArrayLinkedList<Edge> mst = new ArrayLinkedList<>();
    
    public void solve(CSRGraph<Character> g) {
        for (int i = 0; i < g.vs.length; i ++) {
            parent.put(i, i);
        }
        
        for (int i = 0; i < g.vs.length; i ++) {
            for (int j = g.head[i]; j < g.head[i + 1]; j ++) {
                if (!createsCycle(i, g.list[j])) {
                    int to = g.list[j];
                    // Объединяем множества
                    union(i, to);
                    // Добавляем ребро в MST
                    mst.add(new Edge(i, to, 0));
                }
            }
        }
    }
    
    private boolean createsCycle(int u, int v) {
        var rootU = findRoot(u);
        var rootV = findRoot(v);
        // Если корни одинаковые
        // значит ребро создаст цикл
        return rootU == rootV;
    }
    
    private int findRoot(int u) {
        
        int current = u;
        while (current != parent.get(current)) {
            current = parent.get(current);
        }
        
        int root = current;
        
        current = u;
        while (current != parent.get(current)) {
            int next = parent.get(current);
            parent.put(current, root);
            current = next;
            
        }
        
        return root;
    }
    
    private void union(int u, int v) {
        int rootU = findRoot(u);
        int rootV = findRoot(v);
        
        // Если корни разные - объединяем множества
        if (rootU != rootV) {
            // Делаем корень U дочерним корня V
            parent.put(rootU, rootV);
        }
    }

    public static void main(String[] args) {
        CSRGraph<Character> g = new CSRGraph<Character>(new Character[]{'a', 'b', 'c', 'd'},
                // { верш. 1, верш. 2, вес }
                // инициализация структыры с оглавлением 
                // выполняется списком ребер отсортированых по весу
                new Edge[] {new Edge(0, 1, 0), new Edge(0, 2, 1),  new Edge(0, 3, 2),
                        new Edge(1, 2, 3),  new Edge(1, 3, 4),
                        new Edge(2, 1, 5), new Edge(2, 3, 6)});
                        
                               
        
        Kraskal kraskal = new Kraskal();
        kraskal.solve(g);
        
        for (int i = 0; i < kraskal.mst.getSize(); i ++) {
            var e = kraskal.mst.get(i);
            System.out.println(g.vs[e.source()] + " - " + g.vs[e.target()]);
        }
    }
}
