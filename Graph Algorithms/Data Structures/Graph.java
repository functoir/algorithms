import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * FILE: Graph.java
 * Defines an interface for Graphs, with both directed and undirected edge capabilities.
 *
 * @param <V>	the type of vertices
 * @param <E>	the type of edge labels

 * @author Amittai J. Wekesa (@siavava)
 */
public interface Graph<V,E extends Comparable<E>> {

    /** Wrapper class to export edges */
    interface Edge<V,E extends Comparable<E>> extends Comparable<Edge<V,E>> {
        /** Get weight of edge */
        E getWeight();

        /** get vertex that edge points to */
        V getHead();

        /** get source of vertex */
        V getTail();

    }

    /** Number of vertices in Graph */
    int numVertices();

    /** Number of edges in Graph */
    int numEdges();

    /** Iterable collection of vertices in Graph */
    Iterable<V> vertices();

    /** Occurrence of v as a vertex in Graph */
    boolean hasVertex(V v);

    /** Out-degree of vertex in Graph */
    int outDegree(V v);

    /** Get the overall popularity of a vertex
     Returns INFINITY for nonexistent paths */
    int getDistance(V start, V end);

    /** Get distances of other vertices from this vertex */
    Map<V, Integer> getDistances(V v);

    /** In-degree of vertex in Graph */
    int inDegree(V v);

    /** Whether vertex has outbound edges */
    boolean hasOut(V v);

    Iterable<Edge<V,?>> getEdges();

    Queue<Edge<V,?>> getEdgesOrdered();

    /* whether vertex has inward edges */
    boolean hasIn(V v);

    /** Iterable collection vertices with with edges from the vertex */
    Iterable<V> outNeighbors(V v);

    /** Iterable collection of vertices with edges to the vertex */
    Iterable<V> inNeighbors(V v);

    /** Check for edge occurrence */
    boolean hasEdge(V u, V v);

    /** Get label of edge from u to v */
    E getLabel(V u, V v);

    /** Insert vertex into Graph */
    void insertVertex(V v);

    void insertVertexByEdge(Edge<V, E> newEdge);

    void reconstruct(List<Edge<V,E>> edges);

    /** Insert directed edge from u to v  */
    void insertDirected(V u, V v, E e);

    /** Insert undirected edge between u and v  */
    void insertUndirected(V u, V v, E e);

    /** Delete vertex and incident edges from Graph */
    void removeVertex(V v);

    /** Remove the directed edge from u to v */
    void removeDirected(V u, V v);

    /** Remove the undirected edge from u to v */
    void removeUndirected(V u, V v);
}
