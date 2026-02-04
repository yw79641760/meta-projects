package com.megatron.shared.meta.commons.data.model.algo.graph.dag;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * SimpleMutableDAG
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:14 AM
 */
public class SortedVertexMutableDAG<Vertex, VertexInfo, Edge, EdgeInfo>
        extends BaseMutableDAG<Vertex, VertexInfo, Edge, EdgeInfo> {

    private static final long serialVersionUID = -2843711945613027569L;

    public SortedVertexMutableDAG() {
        super();
        this.vertexMap = new HashMap<Vertex, VertexInfo>();
        this.edgeMap = new HashMap<Edge, EdgeInfo>();
        this.outDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
        this.inDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
    }

    public SortedVertexMutableDAG(Map<Vertex, VertexInfo> vertexMap,
                                  Map<Edge, EdgeInfo> edgeMap,
                                  Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                                  Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }

    protected boolean addDegreeIfAbsent(Vertex fromVertex, Vertex toVertex, Edge edge, EdgeInfo edgeInfo, Map<Vertex, Map<Vertex, Edge>> edgeMap) {
        Map<Vertex, Edge> outEdgeMap = edgeMap.computeIfAbsent(fromVertex, k -> new TreeMap<Vertex, Edge>());
        outEdgeMap.put(toVertex, edge);
        return true;
    }
}
