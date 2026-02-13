package com.softmegatron.shared.meta.commons.data.ext.algo.graph.dag;

import java.util.HashMap;
import java.util.Map;

/**
 * SimpleMutableDAG
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:14 AM
 */
public class SimpleMutableDAG<Vertex, VertexInfo, Edge, EdgeInfo>
        extends BaseMutableDAG<Vertex, VertexInfo, Edge, EdgeInfo> {

    private static final long serialVersionUID = 3297468463980449561L;

    public SimpleMutableDAG() {
        super();
        this.vertexMap = new HashMap<Vertex, VertexInfo>();
        this.edgeMap = new HashMap<Edge, EdgeInfo>();
        this.outDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
        this.inDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
    }

    public SimpleMutableDAG(Map<Vertex, VertexInfo> vertexMap,
                            Map<Edge, EdgeInfo> edgeMap,
                            Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                            Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }

    protected boolean addDegreeIfAbsent(Vertex fromVertex, Vertex toVertex, Edge edge, EdgeInfo edgeInfo, Map<Vertex, Map<Vertex, Edge>> edgeMap) {
        Map<Vertex, Edge> outEdgeMap = edgeMap.computeIfAbsent(fromVertex, k -> new HashMap<Vertex, Edge>());
        outEdgeMap.put(toVertex, edge);
        return true;
    }
}
