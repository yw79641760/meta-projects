package com.megatron.shared.meta.commons.data.model.algo.graph.dag;

import java.util.HashMap;
import java.util.Map;

/**
 * SortedVertexDAG
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:17 AM
 */
public class SortedVertexDAG<Vertex, VertexInfo, Edge, EdgeInfo> extends BaseDAG<Vertex, VertexInfo, Edge, EdgeInfo>{

    private static final long serialVersionUID = -5865428284162432624L;

    public SortedVertexDAG() {
        super();
        this.vertexMap = new HashMap<Vertex, VertexInfo>();
        this.edgeMap = new HashMap<Edge, EdgeInfo>();
        this.outDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
        this.inDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
    }

    public SortedVertexDAG(Map<Vertex, VertexInfo> vertexMap,
                           Map<Edge, EdgeInfo> edgeMap,
                           Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                           Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }
}
