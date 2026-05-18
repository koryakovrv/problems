package org.example.dp;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.example.checker.Solution;

public class BigIsland implements Solution {
    
    private int [][] a;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    
    @Override
    public String solve(List<String> args) {
        int n = Integer.parseInt(args.get(0));
        a = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            String[] arg = args.get(i + 1).split(" ");
            for (int j = 0; j < n; j++) {
                a[i][j] = Integer.parseInt(arg[j]);
            }
        }
        
        return String.valueOf(slove());
    }
    
    public int slove() {
        int islands = 0;
        
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                if (a[i][j] == 1) {
                    islands ++;
                    bfs(i, j);
                }
            }
        }
        
        return islands;
    }
    
    private void bfs(int startX, int startY) {
        int n = a.length;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        a[startX][startY] = 2;
        
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0];
            int y = cell[1];
            
            for (int d = 0; d < dx.length; d ++) {
                int i = x + dx[d];
                int j = y + dy[d];
                if (i >= 0 && i < n  && j < n && j >= 0 && a[i][j] == 1) {
                    a[i][j] = 2;
                    queue.add(new int[]{i, j});
                }
            }
        }
    }
}
