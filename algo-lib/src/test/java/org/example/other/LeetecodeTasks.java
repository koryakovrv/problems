package org.example.other;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LeetecodeTasks {
    
    @ParameterizedTest
    @MethodSource("provideTopKFrequentData")
    void topKFrequent(int[] nums, int k, int[] expected) {
        short[] numCounts = new short [20_001];

        int[] kres = new int [k];
        
        int maxCountInd = 0;
        
        for (int i = 0; i < nums.length; i ++) {
            numCounts[nums[i] + 10_000] ++;
            maxCountInd = Math.max(maxCountInd, numCounts[nums[i] + 10_000]);
        }
        
        List<Integer>[] res = new List[maxCountInd + 1];
        
        for (int i = 0; i < res.length; i ++) {
            res[i] = new LinkedList<Integer>();
        }
        
        for (int i = numCounts.length - 1; i >= 0; i --) {
            res[numCounts[i]].add(i - 10_000);
        }

        int j = 0;
        int i = res.length - 1;
        while (j < k) {
            for (Integer l : res[i --] ) {
                kres[j ++] = l;
                if (j == k) break;
            }
        }
        
        assertArrayEquals(expected, kres);
    }
    
    private static Stream<Arguments> provideTopKFrequentData() {
        return Stream.of(
            Arguments.of(new int[] {1, 8, 1, 3, 8, 2, 1, 8, 1, 3}, 3, new int[] {1, 8, 3})
        );
    }
    
    @ParameterizedTest
    @MethodSource("providePathSumData")
    void pathSum(TreeNode root, int targetSum) {
        
        record StackVars(TreeNode node, TreeNode parent, List<Integer> path, int cost) {
        };

        List<List<Integer>> result = new LinkedList<>();
        LinkedList<StackVars> stack = new LinkedList<>();
        LinkedList<Integer> path = new LinkedList<Integer>();
        Map<TreeNode, Integer> childVisited = new HashMap<>();
        
        stack.push(new StackVars(root, null, path, 0));
        
        
        
        while (!stack.isEmpty()) {
            StackVars p = stack.pop();
            p.path.add(p.node.val);
            
            int cost = p.cost + p.node.val;
            
            if (p.node.left != null) {
                stack.push(new StackVars(p.node.left, p.node, p.path, cost));
            }
            if (p.node.right != null) {
                stack.push(new StackVars(p.node.right, p.node, p.path, cost));
            }
            if (p.node.left == null && p.node.right == null) {
                if (targetSum == cost)
                  result.add(new ArrayList<Integer>(path));
                
                p.path.removeLast();
            }
            
            childVisited.put(p.parent(), childVisited.getOrDefault(p.parent(), 0) + 1);
        }
        
       // return result;
    }
    
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.left = left;
            this.right = right;
        }
    }
    
    public static Stream<Arguments> providePathSumData() {
        TreeNode root = new TreeNode(5);
        TreeNode node4 = new TreeNode(4);
        TreeNode node8 = new TreeNode(8);
        root.left = node4;
        root.right = node8;
        TreeNode node11 = new TreeNode(11);
        node4.left = node11;
        TreeNode node7 = new TreeNode(7);
        TreeNode node2 = new TreeNode(2);
        node11.left = node7;
        node11.right = node2;
        TreeNode node13 = new TreeNode(13);
        node8.left = node13;
        TreeNode node_4 = new TreeNode(4);
        TreeNode node_5 = new TreeNode(5);
        TreeNode node1 = new TreeNode(1);
        node8.right = node_4;
        node_4.left = node_5;
        node_4.right = node1;
        
        return Stream.of(
                Arguments.of(root, 22));
    }
}
