package org.example.graphs;

import org.example.hashtable.OpenAddressingHashTable;
import org.example.list.ArrayLinkedList;

public class Kraskal {
    // собственная реализация хэш-таблицы с квадратичным пробингом
    // для системы непересекающихся множеств Union-Find
    private OpenAddressingHashTable<Integer, Integer> parent = 
            new OpenAddressingHashTable<>();
    ArrayLinkedList<Edge> mst = new ArrayLinkedList<>();
    
    private record Edge(int from, int to) {}
    
    public void solve(CSRGraph<String> g) {
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
                    mst.add(new Edge(i, to));
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
        CSRGraph<String> g = new CSRGraph<>(new String[]{"a", "b", "c", "d"},
                // { верш. 1, верш. 2, вес }
                // инициализация структыры с оглавлением 
                // выполняется списком ребер отсортированых по весу
                new int[][] {{0, 1, 0}, {0, 2, 1}, {0, 3, 2},
                             {1, 2, 3}, {1, 3, 4},
                             {2, 1, 5}, {2, 3, 6}});
        
        
        Kraskal kraskal = new Kraskal();
        kraskal.solve(g);
        
        for (int i = 0; i < kraskal.mst.getSize(); i ++) {
            var e = kraskal.mst.get(i);
            System.out.println(g.vs[e.from] + " - " + g.vs[e.to]);
        }
    }
}
