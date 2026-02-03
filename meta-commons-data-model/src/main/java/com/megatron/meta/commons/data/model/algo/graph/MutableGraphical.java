package com.softmegatron.meta.commons.data.model.algo.graph;

/**
 * MutableGraphical 可变图
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 1:00 AM
 */
public interface MutableGraphical<Vertex, VertexInfo, Edge, EdgeInfo>
        extends Graphical<Vertex, VertexInfo, Edge, EdgeInfo> {

    /**
     * 添加顶点
     *
     * @param vertex
     * @param vertexInfo
     * @return
     */
    boolean addVertex(Vertex vertex, VertexInfo vertexInfo);

    /**
     * 添加边
     *
     * @param fromVertex
     * @param toVertex
     * @return
     */
    boolean addEdge(Vertex fromVertex, Vertex toVertex);

    /**
     * 添加边
     *
     * @param fromVertex
     * @param toVertex
     * @param autoCreateVertex
     * @return
     */
    boolean addEdge(Vertex fromVertex, Vertex toVertex, boolean autoCreateVertex);

    /**
     * 添加边
     *
     * @param fromVertex
     * @param toVertex
     * @param edge
     * @param edgeInfo
     * @param autoCreateVertex
     * @return
     */
    boolean addEdge(Vertex fromVertex, Vertex toVertex, Edge edge, EdgeInfo edgeInfo, boolean autoCreateVertex);

    /**
     * 添加边的合法性校验
     * <p>例如：DAG不能出现环</p>
     * 
     * @param fromVertex
     * @param toVertex
     * @param autoCreateVertex
     * @return
     */
    boolean isLegalToAddEdge(Vertex fromVertex, Vertex toVertex, boolean autoCreateVertex);
}
