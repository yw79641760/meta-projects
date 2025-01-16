package com.magi.meta.commons.data.model.algo.graph.dag;

import com.magi.meta.commons.data.model.algo.graph.MutableGraphical;

/**
 * MutableDAG
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 5/4/20 2:03 AM
 */
public interface MutableDAG<Vertex, VertexInfo, Edge, EdgeInfo>
        extends DAG<Vertex, VertexInfo, Edge, EdgeInfo>,
                MutableGraphical<Vertex, VertexInfo, Edge, EdgeInfo> {
}
