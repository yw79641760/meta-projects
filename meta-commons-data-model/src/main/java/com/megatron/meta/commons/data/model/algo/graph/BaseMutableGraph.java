package com.softmegatron.meta.commons.data.model.algo.graph;

import java.util.Map;

/**
 * BaseMutableGraph
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 1:50 AM
 */
public abstract class BaseMutableGraph<Vertex, VertexInfo, Edge, EdgeInfo>
        extends BaseGraph<Vertex, VertexInfo, Edge, EdgeInfo>
        implements MutableGraphical<Vertex, VertexInfo, Edge, EdgeInfo> {

    private static final long serialVersionUID = -6469882737207566728L;

    public BaseMutableGraph() {
        super();
    }

    public BaseMutableGraph(Map<Vertex, VertexInfo> vertexMap,
                            Map<Edge, EdgeInfo> edgeMap,
                            Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                            Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }

    public boolean addVertex(Vertex vertex, VertexInfo vertexInfo) {
        lock.writeLock().lock();
        try {
            getVertexMap().put(vertex, vertexInfo);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean addEdge(Vertex fromVertex, Vertex toVertex) {
        return addEdge(fromVertex, toVertex, false);
    }

    public boolean addEdge(Vertex fromVertex, Vertex toVertex, boolean autoCreateVertex) {
        return addEdge(fromVertex, toVertex, null, null, autoCreateVertex);
    }

    public boolean addEdge(Vertex fromVertex, Vertex toVertex, Edge edge, EdgeInfo edgeInfo, boolean autoCreateVertex) {
        lock.writeLock().lock();
        try {
            // 检查是否可以添加边
            if (!isLegalToAddEdge(fromVertex, toVertex, autoCreateVertex)) {
                return false;
            }
            // 更新顶点信息
            addVertexIfAbsent(fromVertex, null);
            addVertexIfAbsent(toVertex, null);
            // 更新边信息
            if (edge != null) {
                addEdgeIfAbsent(edge, edgeInfo);
            }
            // 更新入度出度信息
            addDegreeIfAbsent(fromVertex, toVertex, edge, edgeInfo, getOutDegreeMap());
            addDegreeIfAbsent(toVertex, fromVertex, edge, edgeInfo, getInDegreeMap());
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 若顶点不存在，则添加顶点信息
     *
     * @param vertex
     * @param vertexInfo
     * @return
     */
    protected boolean addVertexIfAbsent(Vertex vertex, VertexInfo vertexInfo) {
        if (!containsVertex(vertex)) {
            return addVertex(vertex, vertexInfo);
        }
        return true;
    }

    protected boolean addEdgeIfAbsent(Edge edge, EdgeInfo edgeInfo) {
        if (!containsEdge(edge)) {
            getEdgeMap().put(edge, edgeInfo);
            return true;
        }
        return true;
    }

    /**
     * 若入度出度信息学不存在，泽添加入度出度信息
     *
     * @param fromVertex
     * @param toVertex
     * @param edge
     * @param edgeInfo
     * @param edgeMap
     * @return
     */
    abstract protected boolean addDegreeIfAbsent(Vertex fromVertex, Vertex toVertex,
                                                 Edge edge, EdgeInfo edgeInfo,
                                                 Map<Vertex, Map<Vertex, Edge>> edgeMap);
}
