package com.softmegatron.shared.meta.data.ext.algo.graph;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Graphical 图
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 12:45 AM
 */
public interface Graphical<Vertex, VertexInfo, Edge, EdgeInfo> {

    /**
     * 获取所有顶点的集合
     *
     * @return
     */
    Set<Vertex> getAllVertexSet();

    /**
     * 获取所有边的集合
     *
     * @return
     */
    Set<Edge> getAllEdgeSet();

    /**
     * 图中是否存在指定顶点
     *
     * @param vertex
     * @return
     */
    boolean containsVertex(Vertex vertex);

    /**
     * 图中是否存在连接指定两顶点的边
     *
     * @param fromVertex
     * @param toVertex
     * @return
     */
    boolean containsEdge(Vertex fromVertex, Vertex toVertex);

    /**
     * 图中是否存在指定边
     *
     * @param edge
     * @return
     */
    boolean containsEdge(Edge edge);

    /**
     * 获取顶点信息
     *
     * @param vertex
     * @return
     */
    VertexInfo getVertexInfo(Vertex vertex);

    /**
     * 获取边信息
     *
     * @param edge
     * @return
     */
    EdgeInfo getEdgeInfo(Edge edge);

    /**
     * 获取起始顶点集合
     *
     * @return
     */
    List<Vertex> getStartVertexList();

    /**
     * 获取结束顶点集合
     *
     * @return
     */
    List<Vertex> getEndVertexList();

    /**
     * 获取孤立顶点集合
     *
     * @return
     */
    List<Vertex> getIsolatedVertexList();

    /**
     * 获取指定顶点的入度顶点集合
     *
     * @param vertex
     * @return
     */
    Set<Vertex> getProceedingVertexSet(Vertex vertex);

    /**
     * 获取指定顶点的出度顶点集合
     *
     * @param vertex
     * @return
     */
    Set<Vertex> getSubsequenceVertexSet(Vertex vertex);

    /**
     * 获取顶点数
     *
     * @return
     */
    int getVertexCount();

    /**
     * 获取边数
     *
     * @return
     */
    int getEdgeCount();

    /**
     * 获取指定顶点的入度
     *
     * @param vertex
     * @return
     */
    int getVertexInDegree(Vertex vertex);

    /**
     * 获取指定顶点的出度
     *
     * @param vertex
     * @return
     */
    int getVertexOutDegree(Vertex vertex);

    /**
     * 拓扑排序
     * <p>仅有符合DAG的图能返回非空结果</p>
     *
     * @return
     */
    Queue<Vertex> topologicalSort();

    /**
     * 是否存在环形
     *
     * @return
     */
    boolean hasCycle();
}
