package com.softmegatron.shared.meta.commons.data.model.algo.graph.dag;

import com.softmegatron.shared.meta.commons.data.model.algo.graph.BaseMutableGraph;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * BaseMutableDAG
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:06 AM
 */
public abstract class BaseMutableDAG<Vertex, VertexInfo, Edge, EdgeInfo>
        extends BaseMutableGraph<Vertex, VertexInfo, Edge, EdgeInfo>
        implements MutableDAG<Vertex, VertexInfo, Edge, EdgeInfo> {

    private static final long serialVersionUID = -2079579376500890921L;

    protected BaseMutableDAG() {
        super();
    }

    protected BaseMutableDAG(Map<Vertex, VertexInfo> vertexMap,
                          Map<Edge, EdgeInfo> edgeMap,
                          Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                          Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }

    public boolean isLegalToAddEdge(Vertex fromVertex, Vertex toVertex, boolean autoCreateVertex) {
        // 同一顶点
        if (fromVertex.equals(toVertex)) {
            return false;
        }
        // 无顶点信息
        if (!autoCreateVertex
            && (!containsVertex(fromVertex)
            || !containsVertex(toVertex))) {
            return false;
        }
        int vertexCount = getVertexCount();
        Queue<Vertex> queue = new LinkedList<Vertex>();
        queue.add(toVertex);
        // 判断是否存在环形，即查找toVertex的下游顶点是否存在fromVertex
        while (!queue.isEmpty() && (--vertexCount > 0)) {
            Vertex vertex = queue.poll();
            for (Vertex outDegreeVertex : getSubsequenceVertexSet(vertex)) {
                if (outDegreeVertex.equals(fromVertex)) {
                    return false;
                }
                queue.add(outDegreeVertex);
            }
        }
        return true;
    }
}
