package org.example.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ListsEdgesGraphTest {
    
    @Test void simpleTest() {
        ListsEdgesGraph<String> g = new ListsEdgesGraph<>();
        
        g.addV("V1", new String[] {"V2", "V3", "V4"});
        g.addV("V2", new String[] {});
        g.addV("V3", new String[] {"V1", "V2", "V4"});
        g.addV("V4", new String[] {"V1"});
        
        assertEquals(3, g.edgeLists.getFirst().secondElem().getSize());
        assertEquals(1, g.edgeLists.getLast().secondElem().getSize());
    }
}
