package com.magi.meta.commons.data.model.algo.graph.dag;

import java.util.HashMap;
import java.util.Map;

/**
 * SimpleDAG
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 5/4/20 2:11 AM
 */
public class SimpleDAG<Vertex, VertexInfo, Edge, EdgeInfo> extends BaseDAG<Vertex, VertexInfo, Edge, EdgeInfo> {

    private static final long serialVersionUID = -537634488269839438L;

    public SimpleDAG() {
        super();
        this.vertexMap = new HashMap<Vertex, VertexInfo>();
        this.edgeMap = new HashMap<Edge, EdgeInfo>();
        this.outDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
        this.inDegreeMap = new HashMap<Vertex, Map<Vertex, Edge>>();
    }

    public SimpleDAG(Map<Vertex, VertexInfo> vertexMap,
                     Map<Edge, EdgeInfo> edgeMap,
                     Map<Vertex, Map<Vertex, Edge>> outDegreeMap,
                     Map<Vertex, Map<Vertex, Edge>> inDegreeMap) {
        super(vertexMap, edgeMap, outDegreeMap, inDegreeMap);
    }
}
