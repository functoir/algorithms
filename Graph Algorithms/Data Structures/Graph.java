/**
 * FILE: Graph.java
 * Defines an interface for Graphs, with both directed and undirected edge capabilities.
 *
 * @param <V>	the type of vertices
 * @param <E>	the type of edge labels

 * @author Amittai J. Wekesa (@siavava)
 */
public interface Graph<V,E> {
    /* Number of vertices in Graph */
    public int numVertices();

    /*** Number of edges in Graph */
    public int numEdges();

    /** Iterable collection of vertices in Graph */
    public Iterable<V> vertices();

    /** Occurrence of v as a vertex in Graph */
    public boolean hasVertex(V v);

    /** Out-degree of vertex in Graph */
    public int outDegree(V v);

    /** In-degree of vertex in Graph */
    public int inDegree(V v);

    /** Whether vertex has outbound edges */
    public boolean hasOut(V v);

    /* whether vertex has inward edges */
    public boolean hasIn(V v);

    /** Iterable collection vertices with with edges from the vertex */
    public Iterable<V> outNeighbors(V v);

    /** Iterable collection of vertices with edges to the vertex */
    public Iterable<V> inNeighbors(V v);

    /** Check for edge occurrence */
    public boolean hasEdge(V u, V v);

    /** Get label of edge from u to v */
    public E getLabel(V u, V v);

    /** Insert vertex into Graph */
    public void insertVertex(V v);

    /** Insert directed edge from u to v  */
    public void insertDirected(V u, V v, E e);

    /** Insert undirected edge between u and v  */
    public void insertUndirected(V u, V v, E e);

    /** Delete vertex and incident edges from Graph */
    public void removeVertex(V v);

    /** Remove the directed edge from u to v */
    public void removeDirected(V u, V v);

    /** Remove the undirected edge from u to v */
    public void removeUndirected(V u, V v);
}
