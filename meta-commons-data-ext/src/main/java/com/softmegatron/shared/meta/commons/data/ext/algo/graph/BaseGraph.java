package com.softmegatron.shared.meta.commons.data.ext.algo.graph;

import com.softmegatron.shared.meta.commons.data.base.BaseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * BaseGraph
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 1:05 AM
 */
public abstract class BaseGraph<Vertex, VertexInfo, Edge, EdgeInfo>
        extends BaseModel
        implements Graphical<Vertex, VertexInfo, Edge, EdgeInfo>{

    private static final long serialVersionUID = 4039434046367430619L;
    /**
     * 读写锁
     */
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 顶点信息集合
     * <p> vertex -> vertexInfo </p>
     */
    protected Map<Vertex, VertexInfo> vertexMap;
    /**
     * 边信息集合
     * <p> edge -> edgeInfo </p>
     */
    protected Map<Edge, EdgeInfo> edgeMap;
    /**
     * 顶点的出度边信息集合
     * <p> fromVertex -> toVertex -> edge </p>
     */
    protected Map<Vertex, Map<Vertex, Edge>> outDegreeMap;
    /**
     * 顶点的入度边信息集合
     * <p> toVertex -> fromVertex -> edge </p>
     */
    protected Map<Vertex, Map<Vertex, Edge>> inDegreeMap;

    protected BaseGraph() {
        super();
    }

    protected BaseGraph(Map<Vertex, VertexInfo> vertexMap,
                     Map<Edge, EdgeInfo> edgeMap,
                     Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                     Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super();
        this.vertexMap = vertexMap;
        this.edgeMap = edgeMap;
        this.outDegreeMap = outDegreeMap;
        this.inDegreeMap = inDegreeMap;
    }

    public Map<Vertex, VertexInfo> getVertexMap() {
        return vertexMap;
    }

    public void setVertexMap(Map<Vertex, VertexInfo> vertexMap) {
        this.vertexMap = vertexMap;
    }

    public Map<Edge, EdgeInfo> getEdgeMap() {
        return edgeMap;
    }

    public void setEdgeMap(Map<Edge, EdgeInfo> edgeMap) {
        this.edgeMap = edgeMap;
    }

    public Map<Vertex, Map<Vertex, Edge>> getOutDegreeMap() {
        return outDegreeMap;
    }

    public void setOutDegreeMap(Map<Vertex, Map<Vertex, Edge>> outDegreeMap) {
        this.outDegreeMap = outDegreeMap;
    }

    public Map<Vertex, Map<Vertex, Edge>> getInDegreeMap() {
        return inDegreeMap;
    }

    public void setInDegreeMap(Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        this.inDegreeMap = inDegreeMap;
    }

    public Set<Vertex> getAllVertexSet() {
        lock.readLock().lock();
        try {
            return getVertexMap().keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<Edge> getAllEdgeSet() {
        lock.readLock().lock();
        try {
            return getEdgeMap().keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsVertex(Vertex vertex) {
        lock.readLock().lock();
        try {
            return getVertexMap().containsKey(vertex);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsEdge(Vertex fromVertex, Vertex toVertex) {
        lock.readLock().lock();
        try {
            Map<Vertex, Edge> edgeInfoMap = getOutDegreeMap().get(fromVertex);
            if (edgeInfoMap == null
                || edgeInfoMap.size() == 0) {
                return false;
            }
            return edgeInfoMap.containsKey(toVertex);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsEdge(Edge edge) {
        lock.readLock().lock();
        try {
            return getEdgeMap().containsKey(edge);
        } finally {
            lock.readLock().unlock();
        }
    }

    public VertexInfo getVertexInfo(Vertex vertex) {
        lock.readLock().lock();
        try {
            return getVertexMap().get(vertex);
        } finally {
            lock.readLock().unlock();
        }
    }

    public EdgeInfo getEdgeInfo(Edge edge) {
        lock.readLock().lock();
        try {
            return getEdgeMap().get(edge);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Vertex> getStartVertexList() {
        lock.readLock().lock();
        try {
            // outEdgeMap.keySet() - inEdgeMap.keySet()
            final List<Vertex> resultList = new ArrayList<Vertex>();
            final Set<Vertex> inVertexSet = new HashSet<Vertex>(getInDegreeMap().keySet());
            for (Vertex outVertex : getOutDegreeMap().keySet()) {
                if (!inVertexSet.contains(outVertex)) {
                    resultList.add(outVertex);
                }
            }
            return resultList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Vertex> getEndVertexList() {
        lock.readLock().lock();
        try {
            // vertexMap.keySet() - outEdgeMap.keySet()
            final List<Vertex> resultList = new ArrayList<Vertex>();
            final Set<Vertex> outVertexSet = new HashSet<Vertex>(getOutDegreeMap().keySet());
            for (Vertex vertex : getVertexMap().keySet()) {
                if (!outVertexSet.contains(vertex)) {
                    resultList.add(vertex);
                }
            }
            return resultList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Vertex> getIsolatedVertexList() {
        lock.readLock().lock();
        try {
            // 孤立顶点：既不在任何边的起点也不在任何边的终点
            final List<Vertex> resultList = new ArrayList<Vertex>();
            final Set<Vertex> connectedVertices = new HashSet<Vertex>();
            
            // 收集所有连接的顶点（边的起点和终点）
            for (Map.Entry<Vertex, Map<Vertex, Edge>> entry : getOutDegreeMap().entrySet()) {
                connectedVertices.add(entry.getKey()); // 边的起点
                connectedVertices.addAll(entry.getValue().keySet()); // 边的终点
            }
            for (Map.Entry<Vertex, Map<Vertex, Edge>> entry : getInDegreeMap().entrySet()) {
                connectedVertices.add(entry.getKey()); // 边的终点
                connectedVertices.addAll(entry.getValue().keySet()); // 边的起点
            }
            
            // 找出未连接的顶点
            for (Vertex vertex : getVertexMap().keySet()) {
                if (!connectedVertices.contains(vertex)) {
                    resultList.add(vertex);
                }
            }
            return resultList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<Vertex> getProceedingVertexSet(Vertex vertex) {
        lock.readLock().lock();
        try {
            return getAdjacentVertexSet(vertex, getInDegreeMap());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<Vertex> getSubsequenceVertexSet(Vertex vertex) {
        lock.readLock().lock();
        try {
            return getAdjacentVertexSet(vertex, getOutDegreeMap());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 获取指定顶点的临近顶点
     *
     * @param vertex
     * @param edgeMap
     * @return
     */
    protected Set<Vertex> getAdjacentVertexSet(Vertex vertex, Map<Vertex, Map<Vertex, Edge>> edgeMap) {
        final Map<Vertex, Edge> adjacentEdgeMap = edgeMap.get(vertex);
        if (adjacentEdgeMap == null
            || adjacentEdgeMap.size() == 0) {
            return Collections.emptySet();
        }
        return adjacentEdgeMap.keySet();
    }

    public int getVertexCount() {
        lock.readLock().lock();
        try {
            return getVertexMap().size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getEdgeCount() {
        lock.readLock().lock();
        try {
            int result = 0;
            for (Entry<Vertex, Map<Vertex, Edge>> entry : getOutDegreeMap().entrySet()) {
                result += entry.getValue().size();
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getVertexInDegree(Vertex vertex) {
        lock.readLock().lock();
        try {
            return getProceedingVertexSet(vertex).size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getVertexOutDegree(Vertex vertex) {
        lock.readLock().lock();
        try {
            return getSubsequenceVertexSet(vertex).size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Queue<Vertex> topologicalSort() {
        Queue<Vertex> resultQueue = new LinkedBlockingQueue<Vertex>();
        Queue<Vertex> zeroInDegreeQueue = new LinkedBlockingQueue<Vertex>();
        // vertex -> inDegree
        Map<Vertex, Integer> inDegreeCountMap = new HashMap<Vertex, Integer>();
        // 遍历所有节点
        for (Vertex vertex : getVertexMap().keySet()) {
            int inDegree = getVertexInDegree(vertex);
            if (inDegree == 0) {
                zeroInDegreeQueue.add(vertex);
                resultQueue.add(vertex);
            } else {
                inDegreeCountMap.put(vertex, inDegree);
            }
        }
        if (zeroInDegreeQueue.isEmpty()) {
            return zeroInDegreeQueue;
        }
        while(!zeroInDegreeQueue.isEmpty()) {
            Vertex vertex = zeroInDegreeQueue.poll();
            for (Vertex subSequentVertex : getSubsequenceVertexSet(vertex)) {
                Integer inDegree = inDegreeCountMap.get(subSequentVertex);
                if (--inDegree == 0) {
                    resultQueue.add(subSequentVertex);
                    zeroInDegreeQueue.add(subSequentVertex);
                    inDegreeCountMap.remove(subSequentVertex);
                } else {
                    inDegreeCountMap.put(subSequentVertex, inDegree);
                }
            }
        }
        return inDegreeCountMap.size() == 0 ? resultQueue : new LinkedBlockingQueue<Vertex>();
    }

    public boolean hasCycle() {
        lock.readLock().lock();
        try {
            return topologicalSort().isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }
}
