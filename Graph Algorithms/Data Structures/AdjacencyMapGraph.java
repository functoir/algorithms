import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Adjacency-Map implementation of the Graph interface
 * Edge labels are stored in nested maps: { v1 -> { v2 -> edge } }
 *      Weighted Graphs can be implemented as having costs as edge labels.
 *      V -> vertex data type
 *      E -> edge data type
 *
 *      These data types must be specified during declaration of the Graph.
 *
 * @author Amittai J. Wekesa (@siavava)
 */

public class AdjacencyMapGraph<V,E extends Comparable<E>> implements Graph<V,E> {
    protected Map<V, Map<V, E>> out;		// out-edges v1 to v2: { v1 -> { v2 -> edge } }
    protected Map<V, Map<V, E>> in;		    // in-edges v2: { v1 -> { v2 -> edge } }
    protected Map<V, Map<V,Integer>> distances;   // For A* search; check how far one is from the other a vertex is.
    protected int indexedVertices;
    protected Set<Edge<V,?>> edges;

    public static class Edge<V,E extends Comparable<E>> implements Graph.Edge<V,E> {
        V from, to;
        E weight;

        public Edge(V from, V to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public E getWeight() {
            return this.weight;
        }

        @Override
        public V getHead() {
            return this.to;
        }

        @Override
        public V getTail() {
            return this.from;
        }

        @Override
        public int compareTo(@NotNull Graph.Edge<V, E> o) {
            return this.getWeight().compareTo(o.getWeight());
        }

        @Override
        public String toString() {
            return "Edge from: " + from + ", to: " + to + ", weight: " + weight;
        }
    }


    /**
     * Default constructor, creating an empty graph
     */
    public AdjacencyMapGraph() {
        in = new HashMap<>();
        out = new HashMap<>();
        distances = null;
    }

    /**
     * Calculate the number of vertices in the Graph
     * @return integer num of vertices
     */
    public int numVertices() {
        return out.keySet().size();
    }

    /**
     * check if vertex has outward edges
     * @param v: vertex
     * @return true or false
     */
    public boolean hasOut(V v) {
        return out.containsKey(v);
    }

    /**
     * check if vertex has inward edges
     * @param v: vertex
     * @return true or false
     */
    public boolean hasIn(V v) {
        return in.containsKey(v);
    }

    public int numEdges() {
        return out.keySet().size();
    }

    public Iterable<V> vertices() {
        return out.keySet();
    }

    public boolean hasVertex(V v) {
        return out.containsKey(v);
    }

    public int outDegree(V v) {
        return out.get(v).size();
    }

    /**
     * Total in-degree of Graph
     * @param v: vertex
     * @return integer value of vertex's out-degree
     */
    public int inDegree(V v) {
        return in.get(v).size();
    }

    private void computeDistances() {
        if ( (this.distances == null) ||
                this.indexedVertices != ((Collection<V>) vertices()).size()) {
            this.distances = GraphLib.FloydWarshallAPSP(this);
            distances = GraphLib.FloydWarshallAPSP(this);
            this.indexedVertices = ((Collection<V>) vertices()).size();
        }
    }

    public int getDistance(V start, V end) {
        /* if distances not yet initialized OR new vertices have been added, rebuild */
        computeDistances();
        return this.distances.getOrDefault(start, new HashMap<>()).getOrDefault(end, Integer.MAX_VALUE);
    }

    public Map<V, Integer> getDistances(V start) {
        computeDistances();
        return this.distances.getOrDefault(start, new HashMap<>());
    }

    /**
     * Get iterable set of all out-neighbors of a vertex
     * @param v: vertex
     * @return iterable set of vertex's out-neighbors.
     */
    public Iterable<V> outNeighbors(V v) {
        return out.get(v).keySet();
    }

    @Override
    public Iterable<Graph.Edge<V,?>> getEdges() {
        Set<Graph.Edge<V,? extends Comparable<?>>> edges = new HashSet<>();
        for (V u : vertices()) {
            for (V v : outNeighbors(u)) {
                edges.add(new Edge<>(u, v, getLabel(u, v)));
            }
        }
        return edges;
    }

    @Override
    public Queue<Graph.Edge<V, ?>> getEdgesOrdered() {
        Queue<Graph.Edge<V,? extends Comparable<?>>> ordering = new PriorityQueue<>();
        for (V u : vertices()) {
            for (V v : outNeighbors(u)) {
                ordering.add(new Edge<>(u, v, getLabel(u, v)));
            }
        }
        return ordering;
    }

    /**
     * Get iterable set of all in-neighbors
     * @param v: vertex
     * @return set of all out-neighbors
     */
    public Iterable<V> inNeighbors(V v) {
        return in.get(v).keySet();
    }

    /**
     * Check for occurrence of an edge from u to v (directed or undirected)
     * @param u: source vertex
     * @param v: dest. vertex
     * @return true or false
     */
    public boolean hasEdge(V u, V v) {
        return out.get(u).containsKey(v);
    }

    /**
     * Get label of edge from u to v
     * @param u: source vertex
     * @param v: dest. vertex
     * @return label of vertex -> depends on predefined data type of edges
     */
    public E getLabel(V u, V v) {
        return out.get(u).get(v);
    }

    /**
     * insert vertex into Graph
     * @param v: vertex to insert
     */
    public void insertVertex(V v) {
        if (!out.containsKey(v)) {
            out.put(v, new HashMap<>());		// edges from v
            in.put(v, new HashMap<>());			// edges to v
        }
    }

    @Override
    public void insertVertexByEdge(Graph.Edge<V, E> newEdge) {
        V head = newEdge.getHead();
        V tail = newEdge.getTail();
        E label = newEdge.getWeight();

        insertVertex(head);
        insertVertex(tail);
        insertUndirected(tail, head, label);
    }

    @Override
    public void reconstruct(List<Graph.Edge<V, E>> edges) {
        edges.forEach(this::insertVertexByEdge);
    }

    /**
     * Insert a directed edge into Graph
     * @param u: source vertex
     * @param v: dest. vertex
     * @param e: edge label
     */
    public void insertDirected(V u, V v, E e) {
        out.get(u).put(v, e);
        in.get(v).put(u, e);
    }

    /**
     * Insert an undirected edge into Graph
     * @param u: source vertex
     * @param v: destination vertex
     * @param e: edge label
     */
    public void insertUndirected(V u, V v, E e) {
        // insert in both directions
        insertDirected(u, v, e);
        insertDirected(v, u, e);
    }

    /**
     * Delete vertex from Graph
     * @param v: vertex
     */
    public void removeVertex(V v) {
        if (!out.containsKey(v)) return;
        // remove v from all adjacency lists for other vertices
        for (V u : inNeighbors(v)) { // u has an edge to v
            out.get(u).remove(v);
        }
        for (V w : outNeighbors(v)) { // w has an edge from v
            in.get(w).remove(v);
        }
        in.remove(v);
        out.remove(v);
    }

    /**
     * Remove a directed edge from Graph
     * @param u: edge source vertex
     * @param v: edge dest. vertex
     */
    public void removeDirected(V u, V v) {
        in.get(v).remove(u);
        out.get(u).remove(v);
    }

    /**
     * Remove an undirected edge from Graph
     * @param u: source vertex
     * @param v: dest. vertex
     */
    public void removeUndirected(V u, V v) {
        // remove in both directions
        removeDirected(u, v);
        removeDirected(v, u);
    }

    /**
     * Create a string representation of Graph
     * @return string representation of Graph
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (V u : vertices()) {
            str.append(u).append(" -> { ");
            for (V v : outNeighbors(u)) {
                str.append(" ").append(v).append("=").append(getLabel(u, v)).append(", ");
            }
            str.append("}\n");
        }
        return String.valueOf(str);
    }
}
