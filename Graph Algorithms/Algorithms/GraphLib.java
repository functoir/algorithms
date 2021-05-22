import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * A library of Graph algorithms.
 * @param <V> vertex data type
 * @param <E> edge data type
 * @author Amittai J. Wekesa (@siavava)
 */
public class GraphLib<V,E> {

    public Map<V,V> bfs(Graph<V,E> G, V start) {
        System.out.println("Breadth-First Search from " + start);

        /* initialize variables */
        Map<V,V> backTrack = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        backTrack.put(start, null);     // add start vertex to backtrack
        queue.add(start);               // add start to queue
        visited.add(start);             // mark start as visited

        while (!queue.isEmpty()) {              // repeat until queue is empty
            V u = queue.remove();               // dequeue
            for (V v : G.outNeighbors(u)) {     // get all neighbors of current vertex
                if (!visited.contains(v)) {     // if neighbor not visited, visit.
                    visited.add(v);             // remember neighbor has been visited.
                    queue.add(v);               // add to stack (so we can get vertex's neighbors)
                    backTrack.put(v, u);        // add edge to backtrace
                }
            }
        }

        /* return generated paths */
        return backTrack;
    }
    public Map<V,V> dfs(Graph<V,E> G, V start) {
        System.out.println("Depth-First Search from " + start);

        /* initialize variables */
        Map<V,V> backTrack = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Stack<V> stack = new Stack<>();

        backTrack.put(start, null);     // add start to backtrack
        visited.add(start);             // mark start as visited

        stack.push(start);                          // add start to stack
        while (!stack.isEmpty()) {                  // while stack is not empty...
            V u = stack.pop();                      // pop from top of stack
            if (!visited.contains(u)) {             // if vertex not yet visited;
                visited.add(u);                     // visit vertex, mark as visited
                for (V v : G.outNeighbors(u)) {     // get neighbors
                    if (!visited.contains(v)) {     // if neighbor not visited,
                        stack.push(v);              // add to stack
                        backTrack.put(v, u);        // remember traversal path
                    }
                }
            }
        }

        /* return generated paths */
        return backTrack;
    }

    /**
     * Kahn's topological sorting algorithm
     * Returns a topological ordering of a Graph
     *
     * @param G: Graph to sort
     * @return a topological ordering of a Graph, or null if Graph is cyclic
     */
    public Queue<V> TopoSort(Graph<V,E> G) {

        /*
         * create copy of Graph
         * This is important because we need to
         * delete edges and vertices!
         */
        Graph<V,E> copy = copyGraph(G);
        Queue<V> ordering = new LinkedList<>();

        /* while Graph still has vertices */
        int size;                                   // check current number of vertices in Graph
        while( (size = copy.numVertices()) > 0) {   // while Graph still has vertices...
            for (V v : copy.vertices()) {           // loop over vertices
                if (copy.inDegree(v) == 0) {        // add all that have no inward edges to the ordering
                    ordering.add(v);
                    copy.removeVertex(v);           // delete them from Graph.
                }
            }

            /*
             * If number of vertices is unchanged,
             * no vertex was deleted, meaning all vertices have a dependency.
             * Graph MUST be cyclic.
             */
            if (size == copy.numVertices()) {
                System.out.println("Graph is cyclic.\n");
                return null;
            }
        }

        /* return the sequence generated */
        return ordering;
    }

//    public

    /**
     * Floyd-Warshall's All-Pairs-Shortest-Paths algorithm
     * to compute all shortest paths in a Graph.
     * @param G: An adjacency list representation of Graph
     * @return Map of each vertex to Map of adjacent vertices, costs
     *          {vertex -> {adjacent vertex -> shortest path}}
     */
    public Map<V, Map<V, Integer>> FloydWarshallAPSP(Graph<V,E> G) {
        int n = G.numVertices();
        int[][][] OPT = new int[n+1][n+1][n+1];
        List<V> vertices = (List<V>) G.vertices();

        /* initial step: calculate costs direct connections */
        for (int i=1; i<=n; i++) {
            for (int j=1; j<=n; j++) {
                if (i == j) {
                    OPT[i][j][0] = 0;
                }
                else {

                    /* remember we shifted i and j by 1 */
                    V u = vertices.get(i-1);
                    V v = vertices.get(j-1);
                    if (G.hasEdge(u, v)) {
                        OPT[i][j][0] = (int) G.getLabel(u, v);
                    }
                }
            }
        }

        /* Dynamic Programming Step */
        for (int k=1; k<=n; k++) {
            for (int i=1; i<=n; i++) {
                for (int j=1; j<=n; j++) {
                    OPT[i][j][k] = Math.min(OPT[i][j][k-1], OPT[i][k][k-1]);
                    OPT[i][j][k] = Math.min(OPT[i][j][k], OPT[k][j][k-1]);
                }
            }
        }

        /* Build Mapping of all costs */
        Map<V,Map<V,Integer>> costs = new HashMap<>();

        /* For each vertex */
        for (int i=1; i<n; i++) {

            /* For each other vertex in Graph */
            V u = vertices.get(i);
            for (int j = 0; j < n; j++) {

                /* Get computed cost and save */
                V v = vertices.get(j);
                int cost = OPT[i][j][n];
                Map<V, Integer>currentCosts = costs.getOrDefault(u, new HashMap<>());
                currentCosts.put(v, cost);
                costs.put(u, currentCosts);
            }
        }

        /* return Map of all costs */
        return costs;
    }

    public void Dijkstra() {
        ;
    }

    public void BellmanFord() {
        ;
    }
    public void AStar() {
        ;
    }

    private @NotNull
    Graph<V,E> copyGraph(Graph<V,E> G) {

        /* create copy Graph */
        Graph<V,E> copy = new AdjacencyMapGraph<>();

        /* copy all vertices */
        for (V u : G.vertices()) {
            copy.insertVertex(u);
        }

        /* copy all edges */
        for (V u : G.vertices()) {
            /* get neighbors, edges and copy them */
            for (V v : G.outNeighbors(u)) {
                E label = G.getLabel(u, v);
                copy.insertDirected(u, v, label);
                /*
                 * NOTE: undirected edges are implemented
                 * as a pair of directed edges.
                 * Therefore, they will be inserted
                 * as a pair of two directed edges.
                 */
            }
        }

        /* return the copy of the Graph */
        return copy;
    }
}
