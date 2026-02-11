package com.softmegatron.shared.meta.commons.data.ext.algo.graph.dag;

import com.softmegatron.shared.meta.commons.data.ext.algo.graph.MutableGraphical;

/**
 * MutableDAG
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:03 AM
 */
public interface MutableDAG<Vertex, VertexInfo, Edge, EdgeInfo>
        extends DAG<Vertex, VertexInfo, Edge, EdgeInfo>,
                MutableGraphical<Vertex, VertexInfo, Edge, EdgeInfo> {
}
