package com.softmegatron.shared.meta.commons.data.ext.algo.graph.dag;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SimpleMutableDAG 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 SimpleMutableDAG 的各种功能
 * @date 2026/2/6 16:35
 * @since 1.0.0
 */
public class SimpleMutableDAGTest {

    @Test
    public void testEmptyDAG() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        assertEquals("空DAG的顶点数应为0", 0, dag.getVertexCount());
        assertEquals("空DAG的边数应为0", 0, dag.getEdgeCount());
    }

    @Test
    public void testAddVertex() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        assertTrue("添加顶点应成功", dag.addVertex("A", "Info A"));
        assertEquals("顶点数应为1", 1, dag.getVertexCount());
        assertTrue("应包含顶点A", dag.containsVertex("A"));
        assertEquals("顶点信息应正确", "Info A", dag.getVertexInfo("A"));
    }

    @Test
    public void testAddEdge() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addVertex("A", "Info A");
        dag.addVertex("B", "Info B");
        assertTrue("添加边应成功", dag.addEdge("A", "B"));
        assertEquals("边数应为1", 1, dag.getEdgeCount());
        assertTrue("应包含边A->B", dag.containsEdge("A", "B"));
    }

    @Test
    public void testAddEdgeWithAutoCreateVertex() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        assertTrue("自动创建顶点应成功", dag.addEdge("A", "B", true));
        assertEquals("顶点数应为2", 2, dag.getVertexCount());
        assertEquals("边数应为1", 1, dag.getEdgeCount());
        assertTrue("应包含顶点A", dag.containsVertex("A"));
        assertTrue("应包含顶点B", dag.containsVertex("B"));
        assertTrue("应包含边A->B", dag.containsEdge("A", "B"));
    }

    @Test
    public void testAddEdgeWithEdgeAndEdgeInfo() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addVertex("A", "Info A");
        dag.addVertex("B", "Info B");
        assertTrue("添加边和边信息应成功", dag.addEdge("A", "B", "Edge AB", "Edge Info AB", false));
        assertEquals("边数应为1", 1, dag.getEdgeCount());
        assertTrue("应包含边Edge AB", dag.containsEdge("Edge AB"));
        assertEquals("边信息应正确", "Edge Info AB", dag.getEdgeInfo("Edge AB"));
    }

    @Test
    public void testAddEdgeToSelf() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addVertex("A", "Info A");
        assertFalse("添加自环边应失败", dag.addEdge("A", "A", true));
        assertEquals("边数应为0", 0, dag.getEdgeCount());
    }

    @Test
    public void testGetAllVertexSet() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("C", "D", true);
        assertTrue("getAllVertexSet应包含所有顶点", dag.getAllVertexSet().size() == 4 &&
                   dag.getAllVertexSet().contains("A") && dag.getAllVertexSet().contains("B") &&
                   dag.getAllVertexSet().contains("C") && dag.getAllVertexSet().contains("D"));
    }

    @Test
    public void testGetAllEdgeSet() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addVertex("A", "Info A");
        dag.addVertex("B", "Info B");
        dag.addEdge("A", "B", "Edge AB", "Info", false);
        assertTrue("getAllEdgeSet应包含边", dag.getAllEdgeSet().size() == 1 && dag.getAllEdgeSet().contains("Edge AB"));
    }

    @Test
    public void testGetStartVertexList() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("C", "D", true);
        assertTrue("getStartVertexList应包含A和C", dag.getStartVertexList().size() == 2 &&
                   dag.getStartVertexList().contains("A") && dag.getStartVertexList().contains("C"));
    }

    @Test
    public void testGetEndVertexList() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("C", "D", true);
        assertTrue("getEndVertexList应包含B和D", dag.getEndVertexList().size() == 2 &&
                   dag.getEndVertexList().contains("B") && dag.getEndVertexList().contains("D"));
    }

    @Test
    public void testGetIsolatedVertexList() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addVertex("C", "Info C");
        assertTrue("getIsolatedVertexList应包含C", dag.getIsolatedVertexList().size() == 1 && dag.getIsolatedVertexList().contains("C"));
    }

    @Test
    public void testGetProceedingVertexSet() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("C", "B", true);
        assertTrue("getProceedingVertexSet(B)应包含A和C", dag.getProceedingVertexSet("B").size() == 2 &&
                   dag.getProceedingVertexSet("B").contains("A") && dag.getProceedingVertexSet("B").contains("C"));
    }

    @Test
    public void testGetSubsequenceVertexSet() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("A", "C", true);
        assertTrue("getSubsequenceVertexSet(A)应包含B和C", dag.getSubsequenceVertexSet("A").size() == 2 &&
                   dag.getSubsequenceVertexSet("A").contains("B") && dag.getSubsequenceVertexSet("A").contains("C"));
    }

    @Test
    public void testGetVertexInDegree() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("C", "B", true);
        assertEquals("B的入度应为2", 2, dag.getVertexInDegree("B"));
    }

    @Test
    public void testGetVertexOutDegree() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("A", "C", true);
        assertEquals("A的出度应为2", 2, dag.getVertexOutDegree("A"));
    }

    @Test
    public void testMultipleEdgesFromOneVertex() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addVertex("A", "Info A");
        dag.addVertex("B", "Info B");
        dag.addVertex("C", "Info C");
        dag.addVertex("D", "Info D");
        dag.addEdge("A", "B", false);
        dag.addEdge("A", "C", false);
        dag.addEdge("A", "D", false);
        assertEquals("A的出度应为3", 3, dag.getVertexOutDegree("A"));
    }

    @Test
    public void testMultipleEdgesToOneVertex() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addVertex("A", "Info A");
        dag.addVertex("B", "Info B");
        dag.addVertex("C", "Info C");
        dag.addVertex("D", "Info D");
        dag.addEdge("A", "D", false);
        dag.addEdge("B", "D", false);
        dag.addEdge("C", "D", false);
        assertEquals("D的入度应为3", 3, dag.getVertexInDegree("D"));
    }

    @Test
    public void testComplexDAG() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("A", "C", true);
        dag.addEdge("B", "D", true);
        dag.addEdge("C", "D", true);
        dag.addEdge("D", "E", true);

        assertEquals("顶点数应为5", 5, dag.getVertexCount());
        assertEquals("边数应为5", 5, dag.getEdgeCount());
        assertEquals("起始顶点数应为1", 1, dag.getStartVertexList().size());
        assertEquals("结束顶点数应为1", 1, dag.getEndVertexList().size());
        assertFalse("不应有环", dag.hasCycle());
    }

    @Test
    public void testDuplicateEdge() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        dag.addEdge("A", "B", true);
        dag.addEdge("A", "B", false);
        assertEquals("添加重复边不应增加边数", 1, dag.getEdgeCount());
    }

    @Test
    public void testEmptyGraphGetters() {
        SimpleMutableDAG<String, String, String, String> dag = new SimpleMutableDAG<>();
        assertNotNull("getVertexMap不应为null", dag.getVertexMap());
        assertNotNull("getEdgeMap不应为null", dag.getEdgeMap());
        assertNotNull("getOutDegreeMap不应为null", dag.getOutDegreeMap());
        assertNotNull("getInDegreeMap不应为null", dag.getInDegreeMap());
        assertTrue("getAllVertexSet应为空", dag.getAllVertexSet().isEmpty());
        assertTrue("getAllEdgeSet应为空", dag.getAllEdgeSet().isEmpty());
        assertTrue("getStartVertexList应为空", dag.getStartVertexList().isEmpty());
        assertTrue("getEndVertexList应为空", dag.getEndVertexList().isEmpty());
        assertTrue("getIsolatedVertexList应为空", dag.getIsolatedVertexList().isEmpty());
    }
}
