package org.example.dp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class MaxRectangleArea {
    private static int[] left;
    private static int[] right;
    private static Deque<Integer> stack = new ArrayDeque<>();
    
    public static int findMaxRectangleArea(int N, int M, int[][] obstacles) {
        left = new int[M];
        right = new int[M];
        
        boolean[][] free = new boolean[N][M];
        for (int i = 0; i < N; i++) {
            Arrays.fill(free[i], true);
        }
        
        for (int[] obs : obstacles) {
            int x = obs[0];
            int y = obs[1];
            if (x >= 0 && x < N && y >= 0 && y < M) {
                free[x][y] = false;
            }
        }
        
        int[] heights = new int[M];
        int maxArea = 0;
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (free[i][j]) {
                    heights[j] ++;
                } else {
                    heights[j] = 0;
                }
            }
            stack.clear();
            maxArea = Math.max(maxArea, maxRect(heights));
        }
        
        return maxArea;
    }
    
    private static int maxRect(int[] heights) {
        int n = heights.length;
        
        for (int i = 0; i < n; i ++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        
        stack.clear();
        
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            right[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }
        
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int width = right[i] - left[i] - 1;
            int area = heights[i] * width;
            maxArea = Math.max(maxArea, area);
        }
        
        return maxArea;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        String[] dimensions = reader.readLine().split(" ");
        int N = Integer.parseInt(dimensions[0]);
        int M = Integer.parseInt(dimensions[1]);
        
        int T = Integer.parseInt(reader.readLine());
        
        int[][] obstacles = new int[T][2];
        for (int i = 0; i < T; i ++) {
            String[] coords = reader.readLine().split(" ");
            obstacles[i][0] = Integer.parseInt(coords[0]);
            obstacles[i][1] = Integer.parseInt(coords[1]);
        }

        int result = findMaxRectangleArea(N, M, obstacles);
        System.out.println(result);
    }
}
