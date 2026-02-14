package com.softmegatron.shared.meta.data.ext.algo.graph.dag;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * SimpleDAG 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 SimpleDAG 的各种功能
 * @date 2026/2/6 16:40
 * @since 1.0.0
 */
public class SimpleDAGTest {

    @Test
    public void testEmptyDAG() {
        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>();
        assertEquals("空DAG的顶点数应为0", 0, dag.getVertexCount());
        assertEquals("空DAG的边数应为0", 0, dag.getEdgeCount());
        assertTrue("getAllVertexSet应为空", dag.getAllVertexSet().isEmpty());
        assertTrue("getAllEdgeSet应为空", dag.getAllEdgeSet().isEmpty());
    }

    @Test
    public void testConstructorWithMaps() {
        Map<String, String> vertexMap = new HashMap<>();
        vertexMap.put("A", "Info A");
        vertexMap.put("B", "Info B");

        Map<String, String> edgeMap = new HashMap<>();
        edgeMap.put("Edge AB", "Edge Info AB");

        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdges = new HashMap<>();
        outEdges.put("B", "Edge AB");
        outDegreeMap.put("A", outEdges);

        Map<String, Map<String, String>> inDegreeMap = new HashMap<>();
        Map<String, String> inEdges = new HashMap<>();
        inEdges.put("A", "Edge AB");
        inDegreeMap.put("B", inEdges);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(vertexMap, edgeMap, outDegreeMap, inDegreeMap);

        assertEquals("顶点数应为2", 2, dag.getVertexCount());
        assertEquals("边数应为1", 1, dag.getEdgeCount());
        assertTrue("应包含顶点A", dag.containsVertex("A"));
        assertTrue("应包含顶点B", dag.containsVertex("B"));
        assertTrue("应包含边", dag.containsEdge("Edge AB"));
        assertTrue("应包含边A->B", dag.containsEdge("A", "B"));
    }

    @Test
    public void testGetVertexInfo() {
        Map<String, String> vertexMap = new HashMap<>();
        vertexMap.put("A", "Info A");

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                vertexMap,
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>()
        );

        assertEquals("顶点信息应正确", "Info A", dag.getVertexInfo("A"));
        assertNull("不存在的顶点信息应为null", dag.getVertexInfo("B"));
    }

    @Test
    public void testGetEdgeInfo() {
        Map<String, String> edgeMap = new HashMap<>();
        edgeMap.put("Edge AB", "Edge Info AB");

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                edgeMap,
                new HashMap<>(),
                new HashMap<>()
        );

        assertEquals("边信息应正确", "Edge Info AB", dag.getEdgeInfo("Edge AB"));
        assertNull("不存在的边信息应为null", dag.getEdgeInfo("Unknown Edge"));
    }

    @Test
    public void testContainsVertex() {
        Map<String, String> vertexMap = new HashMap<>();
        vertexMap.put("A", "Info A");

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                vertexMap,
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>()
        );

        assertTrue("应包含存在的顶点", dag.containsVertex("A"));
        assertFalse("不应包含不存在的顶点", dag.containsVertex("B"));
    }

    @Test
    public void testContainsEdge() {
        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdges = new HashMap<>();
        outEdges.put("B", "Edge AB");
        outDegreeMap.put("A", outEdges);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                new HashMap<>(),
                outDegreeMap,
                new HashMap<>()
        );

        assertTrue("应包含存在的边", dag.containsEdge("A", "B"));
        assertFalse("不应包含不存在的边", dag.containsEdge("B", "A"));
    }

    @Test
    public void testGetStartVertexList() {
        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdgesA = new HashMap<>();
        outEdgesA.put("B", "Edge AB");
        outDegreeMap.put("A", outEdgesA);

        Map<String, Map<String, String>> inDegreeMap = new HashMap<>();
        Map<String, String> inEdgesB = new HashMap<>();
        inEdgesB.put("A", "Edge AB");
        inDegreeMap.put("B", inEdgesB);

        // 添加顶点信息
        Map<String, String> vertexMap = new HashMap<>();
        vertexMap.put("A", "Vertex A");
        vertexMap.put("B", "Vertex B");
        
        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                vertexMap,
                new HashMap<>(),
                outDegreeMap,
                inDegreeMap
        );

        assertEquals("起始顶点数应为1", 1, dag.getStartVertexList().size());
        assertTrue("应包含A", dag.getStartVertexList().contains("A"));
    }

    @Test
    public void testGetEndVertexList() {
        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdgesA = new HashMap<>();
        outEdgesA.put("B", "Edge AB");
        outDegreeMap.put("A", outEdgesA);

        Map<String, Map<String, String>> inDegreeMap = new HashMap<>();
        Map<String, String> inEdgesB = new HashMap<>();
        inEdgesB.put("A", "Edge AB");
        inDegreeMap.put("B", inEdgesB);

        // 添加顶点信息
        Map<String, String> vertexMap = new HashMap<>();
        vertexMap.put("A", "Vertex A");
        vertexMap.put("B", "Vertex B");
        
        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                vertexMap,
                new HashMap<>(),
                outDegreeMap,
                inDegreeMap
        );

        assertEquals("结束顶点数应为1", 1, dag.getEndVertexList().size());
        assertTrue("应包含B", dag.getEndVertexList().contains("B"));
    }

    @Test
    public void testGetIsolatedVertexList() {
        Map<String, String> vertexMap = new HashMap<>();
        vertexMap.put("A", "Info A");
        vertexMap.put("B", "Info B");
        vertexMap.put("C", "Info C");

        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdgesA = new HashMap<>();
        outEdgesA.put("B", "Edge AB");
        outDegreeMap.put("A", outEdgesA);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                vertexMap,
                new HashMap<>(),
                outDegreeMap,
                new HashMap<>()
        );

        assertEquals("孤立顶点数应为1", 1, dag.getIsolatedVertexList().size());
        assertTrue("应包含C", dag.getIsolatedVertexList().contains("C"));
    }

    @Test
    public void testGetProceedingVertexSet() {
        Map<String, Map<String, String>> inDegreeMap = new HashMap<>();
        Map<String, String> inEdgesB = new HashMap<>();
        inEdgesB.put("A", "Edge AB");
        inEdgesB.put("C", "Edge CB");
        inDegreeMap.put("B", inEdgesB);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                inDegreeMap
        );

        assertEquals("B的前驱顶点数应为2", 2, dag.getProceedingVertexSet("B").size());
        assertTrue("应包含A和C", dag.getProceedingVertexSet("B").contains("A") &&
                   dag.getProceedingVertexSet("B").contains("C"));
    }

    @Test
    public void testGetSubsequenceVertexSet() {
        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdgesA = new HashMap<>();
        outEdgesA.put("B", "Edge AB");
        outEdgesA.put("C", "Edge AC");
        outDegreeMap.put("A", outEdgesA);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                new HashMap<>(),
                outDegreeMap,
                new HashMap<>()
        );

        assertEquals("A的后继顶点数应为2", 2, dag.getSubsequenceVertexSet("A").size());
        assertTrue("应包含B和C", dag.getSubsequenceVertexSet("A").contains("B") &&
                   dag.getSubsequenceVertexSet("A").contains("C"));
    }

    @Test
    public void testGetVertexInDegree() {
        Map<String, Map<String, String>> inDegreeMap = new HashMap<>();
        Map<String, String> inEdgesB = new HashMap<>();
        inEdgesB.put("A", "Edge AB");
        inEdgesB.put("C", "Edge CB");
        inDegreeMap.put("B", inEdgesB);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                inDegreeMap
        );

        assertEquals("B的入度应为2", 2, dag.getVertexInDegree("B"));
    }

    @Test
    public void testGetVertexOutDegree() {
        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdgesA = new HashMap<>();
        outEdgesA.put("B", "Edge AB");
        outEdgesA.put("C", "Edge AC");
        outDegreeMap.put("A", outEdgesA);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                new HashMap<>(),
                outDegreeMap,
                new HashMap<>()
        );

        assertEquals("A的出度应为2", 2, dag.getVertexOutDegree("A"));
    }

    @Test
    public void testGetEdgeCount() {
        Map<String, Map<String, String>> outDegreeMap = new HashMap<>();
        Map<String, String> outEdgesA = new HashMap<>();
        outEdgesA.put("B", "Edge AB");
        outEdgesA.put("C", "Edge AC");
        outDegreeMap.put("A", outEdgesA);

        Map<String, String> outEdgesD = new HashMap<>();
        outEdgesD.put("E", "Edge DE");
        outDegreeMap.put("D", outEdgesD);

        SimpleDAG<String, String, String, String> dag = new SimpleDAG<>(
                new HashMap<>(),
                new HashMap<>(),
                outDegreeMap,
                new HashMap<>()
        );

        assertEquals("边数应为3", 3, dag.getEdgeCount());
    }
}
