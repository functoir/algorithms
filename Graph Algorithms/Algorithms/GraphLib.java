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
     * @param G: Graph implementing some vertex type and some edge type.
     *         Edge type must extend "Double" datatype (i.e. must be a number).
     * @return Map of each vertex to Map of adjacent vertices, costs
     *          {vertex -> {adjacent vertex -> shortest path}}
     */
    public Map<V, Map<V, Double>> FloydWarshallAPSP(Graph<V, ? extends Double> G) {
        int n = G.numVertices();
        double[][][] OPT = new double[n+1][n+1][n+1];
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
                        OPT[i][j][0] = G.getLabel(u, v);
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
        Map<V,Map<V,Double>> costs = new HashMap<>();

        /* For each vertex */
        for (int i=1; i<n; i++) {

            /* For each other vertex in Graph */
            V u = vertices.get(i);
            for (int j = 0; j < n; j++) {

                /* Get computed cost and save */
                V v = vertices.get(j);
                double cost = OPT[i][j][n];
                Map<V, Double>currentCosts = costs.getOrDefault(u, new HashMap<>());
                currentCosts.put(v, cost);
                costs.put(u, currentCosts);
            }
        }

        /* return Map of all costs */
        return costs;
    }

    /**
     * Dijkstra's algorithm for single-source shortest paths.
     * Returns a Map of all the other vertices connected to the start vertex
     * and their respective shortest costs from start vertices.
     * To compute actual shortest path, use DijkstraPath().
     * @param G: Graph implementing some vertex type and some edge type.
     *         Edge type must extend "Double" datatype (i.e. must be a number).
     * @param start: start vertex
     * @return Map of costs of vertices from start
     */
    public Map<V, Double> Dijkstra(Graph<V, ? extends Double> G, V start) {
        int n = G.numVertices();                    // get num of vertices in Graph
        Map<V, Double> costs = new HashMap<>();     // initialize map of costs
        Map<V, V> backTrack = new HashMap<>();      // initialize backtrack

        /* save start vertex to back-track */
        backTrack.put(start, null);
        costs.put(start, 0.0);

        /* initialize queue of vertices to visit */
        Queue<V> toVisit = new LinkedList<>();

        /* add start vertex */
        toVisit.add(start);

        /*
         * loop from 1 to n-1
         * since vertices can only appear once in a shortest path.
         */
        for (int i=1; i<n; i++) {

            /* track queue of next vertices */
            Queue<V> nextVertices = new LinkedList<>();

            /* while current queue still has vertices, visit them */
            while (!toVisit.isEmpty()) {
                V u = toVisit.remove();

                /* get neighbors */
                for (V v : G.outNeighbors(u)) {

                    /*make sure we don't traverse backwards */
                    if (!backTrack.get(u).equals(v)) {

                        /* get cost of edge */
                        double edgeCost = G.getLabel(u, v);

                        /* get costs of previous vertex */
                        double predecessor = costs.get(u);

                        /* get current cost of vertex. if nonexistent, set to infinity */
                        double currentCost = costs.getOrDefault(v, Double.MAX_VALUE);

                        /* if cost is better than saved score, save it */
                        if (predecessor + edgeCost < currentCost) {
                            costs.remove(v);
                            costs.put(v, predecessor+edgeCost);

                            /* update the backtrack to reflect new min-cost neighbor */
                            backTrack.remove(u);
                            backTrack.put(u, v);

                            /* get next neighbors of current vertex and add to queue */
                            for (V next : G.outNeighbors(v)) {
                                if (!next.equals(u)) {
                                    nextVertices.add(next);
                                }
                            }
                        }
                    }
                }
            }
            /* reset queue to next vertices in line */
            toVisit = nextVertices;
        }
        /* return cost start to end, or INFINITY if no path found */
        return costs;
    }

    /**
     * Dijkstra's algorithm for single-source shortest paths.
     * Returns the shortest path from start vertex to end vertex.
     * To compute costs, use Dijkstra().
     * @param G: Graph
     * @param start: start vertex
     * @return Map of costs of vertices from start
     */
    public List<V> DijkstraPath(Graph<V, ? extends Double> G, V start, V end) {
        int n = G.numVertices();                    // get num of vertices in Graph
        Map<V, Double> costs = new HashMap<>();     // initialize map of costs
        Map<V, V> backTrack = new HashMap<>();      // initialize backtrack

        /* save start vertex to back-track */
        backTrack.put(start, null);
        costs.put(start, 0.0);

        /* initialize queue of vertices to visit */
        Queue<V> toVisit = new LinkedList<>();

        /* add start vertex */
        toVisit.add(start);

        /*
         * loop from 1 to n-1
         * since vertices can only appear once in a shortest path.
         */
        for (int i=1; i<n; i++) {

            /* track queue of next vertices */
            Queue<V> nextVertices = new LinkedList<>();

            /* while current queue still has vertices, visit them */
            while (!toVisit.isEmpty()) {
                V u = toVisit.remove();

                /* get neighbors */
                for (V v : G.outNeighbors(u)) {

                    /*make sure we don't traverse backwards */
                    if (!backTrack.get(u).equals(v)) {

                        /* get cost of edge */
                        double edgeCost = G.getLabel(u, v);

                        /* get costs of previous vertex */
                        double predecessor = costs.get(u);

                        /* get current cost of vertex. if nonexistent, set to infinity */
                        double currentCost = costs.getOrDefault(v, Double.MAX_VALUE);

                        /* if cost is better than saved score, save it */
                        if (predecessor + edgeCost < currentCost) {
                            costs.remove(v);
                            costs.put(v, predecessor+edgeCost);

                            /* update the backtrack to reflect new min-cost neighbor */
                            backTrack.remove(u);
                            backTrack.put(u, v);

                            /* get next neighbors of current vertex and add to queue */
                            for (V next : G.outNeighbors(v)) {
                                if (!next.equals(u)) {
                                    nextVertices.add(next);
                                }
                            }
                        }
                    }
                }
            }
            /* reset queue to next vertices in line */
            toVisit = nextVertices;
        }

        /* rebuild path */
        Stack<V> path = new Stack<>();

        /* push final vertex to path */
        path.push(end);

        /* backTrack and prepend vertices to path */
        V vertex = end;
        while (vertex != null) {
            V pred = backTrack.get(vertex);
            if (pred != null) {
                path.push(pred);
            }
            vertex = pred;
        }

        /* return the path */
        return path;
    }

    public Map<V, Double> BellmanFord(Graph<V, ? extends Double> G, V start) {

        /* initialize variables */
        int n = G.numVertices();
        List<V> vertices = (List<V>) G.vertices();
        Map<V, Double> costs = new HashMap<>();
        costs.put(start, 0.0);

        /* initialize table */
        double[][] OPT = new double[n][n+1];

        /* initialize zero-th iteration */
        for (int i=1; i<=n; i++) {
            OPT[0][i] = Double.MAX_VALUE;
        }

        /* start vertex to itself is 0 */
        OPT[0][vertices.indexOf(start)+1] = 0;

        /* loop 1 to n-1 times */
        for (int i=1; i<n; i++) {

            for (V currentVertex : G.vertices()) {                              // for each vertex...
                int index = vertices.indexOf(currentVertex);
                OPT[i][index] = OPT[i-1][index];                                // get value from previous iteration
            }
            for (V currentVertex : G.vertices()) {                              // for each vertex...
                int index = vertices.indexOf(currentVertex);
                for (V nextVertex : G.inNeighbors(currentVertex)) {             // get outbound neighbors
                    int nextIndex = vertices.indexOf(nextVertex);
                    double transition = G.getLabel(currentVertex, nextVertex);

                    /*
                     * if path from current vertex improves min cost to neighbor,
                     * perform the improvement and save the new cost
                     */

                    if (OPT[i-1][index] + index < OPT[i][nextIndex]) {
                        OPT[i][nextIndex] = OPT[i-1][index] + transition;
                        costs.remove(nextVertex);
                        costs.put(nextVertex, OPT[i][nextIndex]);
                    }
                }
            }
        }

        /* return calculated costs */
        return costs;
    }
    public void AStar() {
        ;
    }

    public @NotNull
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
