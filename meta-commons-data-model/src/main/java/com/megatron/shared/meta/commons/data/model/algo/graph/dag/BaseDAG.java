package com.megatron.shared.meta.commons.data.model.algo.graph.dag;

import com.megatron.shared.meta.commons.data.model.algo.graph.BaseGraph;

import java.util.Map;

/**
 * BaseDAG
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:04 AM
 */
public abstract class BaseDAG<Vertex, VertexInfo, Edge, EdgeInfo>
        extends BaseGraph<Vertex, VertexInfo, Edge, EdgeInfo>
        implements DAG<Vertex, VertexInfo, Edge, EdgeInfo> {

    private static final long serialVersionUID = -7989205073617613875L;

    protected BaseDAG() {
        super();
    }

    protected BaseDAG(Map<Vertex, VertexInfo> vertexMap,
                   Map<Edge, EdgeInfo> edgeMap,
                   Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                   Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }
}
