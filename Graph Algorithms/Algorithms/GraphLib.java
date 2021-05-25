//import net.datastructures.Graph;
import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * A library of Graph algorithms.
 * @author Amittai J. Wekesa (@siavava)
 */
public class GraphLib {

    /**
     * Breadth-First Search
     * @param G: Graph
     * @param start start vertex
     * @param <V> vertex data type
     * @param <E> edge data type
     * @return Map containing vertices and their predecessors.
     *          {V vertex -> V predecessor}
     *          Start vertex points to "null" as predecessor.
     *          Individual paths can be reconstructed by back-tracing.
     */
    public static <V,E> Map<V,V> bfs(Graph<V,E> G, V start) {
        System.out.println("Breadth-First Search from " + start);

        /* initialize variables */
        Map<V,V> backTrack = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();    // better than ArrayList since we add to tail, remove from head

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

    /**
     * Depth-First Search
     * @param G: Graph
     * @param start start vertex
     * @param <V> vertex data type
     * @param <E> edge data type
     * @return Map containing vertices and their predecessors.
     *          {V vertex -> V predecessor}
     *          Start vertex points to "null" as predecessor.
     *          Individual paths can be reconstructed by back-tracing.
     */
    public static <V,E> Map<V,V> dfs(Graph<V,E> G, V start) {
        System.out.println("Depth-First Search from " + start);

        /* initialize variables */
        Map<V,V> backTrack = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Stack<V> stack = new Stack<>();

        backTrack.put(start, null);     // add start to backtrack
//        visited.add(start);             // mark start as visited

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
     * @param G : Graph to sort
     * @return Queue, a topological ordering of a Graph, or null if Graph is cyclic
     */
    public static <V,E> Object TopoSort(Graph<V,E> G) {
        System.out.println("\nRunning Topological Sort on the Graph...\n");

        /*
         * create copy of Graph
         * This is important because we need to
         * delete edges and vertices!
         */
        Graph<V,E> copy = copyGraph(G);
        Queue<V> ordering = new LinkedList<>();

        /* while Graph still has vertices */
        int size = copy.numVertices();                                 // check current number of vertices in Graph
        while(size > 0) {   // while Graph still has vertices...
            int current = size;
            Set<V> removed = new HashSet<>();
            for (V v : copy.vertices()) {           // loop over vertices
                if (copy.inDegree(v) == 0) {        // add all that have no inward edges to the ordering
                    if (ordering.add(v)) {
                        removed.add(v);             // mark for removal
                    }
                }
            }
            for (V v : removed) {
                copy.removeVertex(v);           // delete them from Graph.
                size--;                     // decrement size
            }

            /*
             * If number of vertices is unchanged,
             * no vertex was deleted, meaning all vertices have a dependency.
             * Graph MUST be cyclic.
             */
            if (size == current) {
                return "Graph is cyclic.";
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
    public static <V> Map<V, Map<V, Double>> FloydWarshallAPSP(Graph<V, ? extends Double> G) {
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
     * @param G : Graph implementing some vertex type and some edge type.
     *         Edge type must extend "Double" datatype (i.e. must be a number).
     * @param start : start vertex
     * @return Map of costs of vertices from start
     */
    public static <V,E> Map<V, Integer> Dijkstra(Graph<V,E> G, V start) {
        System.out.println("Dijkstra calculating costs from '" + start + "'." );
        int n = G.numVertices();                    // get num of vertices in Graph
        Map<V,Integer> costs = new HashMap<>();     // initialize map of costs

        /* Initialize the priority queue */
        Queue<V> queue = new PriorityQueue<>(G.numVertices(), Comparator.comparingInt(costs::get));

        /*
         * Add all vertices to priority queue
         */
        for (V v : G.vertices()) {
            if (v != start) {
                costs.put(v, Integer.MAX_VALUE);
                queue.add(v);
            }
        }
        costs.put(start, 0);
        queue.add(start);

        /* repeatedly extract min until queue is empty */
        while (!queue.isEmpty()) {
            V current = queue.remove();
            int curr = costs.get(current);
            for (V next : G.outNeighbors(current)) {
                int currToNext = (int) G.getLabel(current, next);
                if ((curr + currToNext < costs.get(next))) {
                    costs.put(next, (curr + currToNext));
                    queue.remove(next);
                    queue.add(next);
                    System.out.println(next + " : " + (curr + currToNext));
                }
            }
        }
        return costs;
    }

    /**
     * Dijkstra's algorithm for single-source shortest paths.
     * Returns the shortest path from start vertex to end vertex.
     * To compute costs, use Dijkstra().
     * @param G : Graph
     * @param start : start vertex
     * @param end : The goal vertex
     * @return Map of costs of vertices from start
     */
    public static <V,E> Object DijkstraPath(Graph<V,E> G, V start, V end) {
        System.out.println("Dijkstra Pathfinding from '" + start + "' to '" + end + "'." );
        int n = G.numVertices();                    // get num of vertices in Graph
        Map<V, Integer> costs = new HashMap<>();     // initialize map of costs
        Map<V, V> backTrack = new HashMap<>();      // initialize backtrack

        /* save start vertex to back-track */
        backTrack.put(start, null);

        /* Initialize the priority queue */
        Queue<V> queue = new PriorityQueue<>(G.numVertices(), Comparator.comparingInt(costs::get));

        /*
         * Add all vertices to priority queue
         */
        for (V v : G.vertices()) {
            if (v != start) {
                costs.put(v, Integer.MAX_VALUE);
                queue.add(v);
            }
        }
        costs.put(start, 0);
        queue.add(start);

        /* repeatedly extract min until queue is empty */
        while (!queue.isEmpty()) {
            V current = queue.remove();
            int curr = costs.get(current);
            for (V next : G.outNeighbors(current)) {
                int currToNext = (int) G.getLabel(current, next);
                if ((curr + currToNext < costs.get(next))) {
                    costs.put(next, (curr + currToNext));
                    queue.remove(next);
                    queue.add(next);
                    backTrack.put(next, current);

                }
            }
            if (current == end) {
                break;
            }
            else if (costs.get(current) == Integer.MAX_VALUE) {
                return "not found.";
            }
        }

        /* rebuild path */
        List<V> path = new LinkedList<>();

        /* backTrack and prepend vertices to path */
//        V vertex = end;
        for (V vertex=end; vertex != null; vertex=backTrack.get(vertex)) {
            if (backTrack.get(vertex) != null) {
                System.out.println(vertex + " was discovered from " + backTrack.get(vertex));
            }
            path.add(0, vertex);
        }

        /* return reconstructed path */
        return path;
    }

    /**
     * Bellman-Ford algorithm for computing shortest paths from given vertex.
     * @param G Graph. Must implement edge labels as a numerical type.
     * @param start start vertex
     * @return Map of all vertices in the Graph and their distance from start vertex.
     * Returns infinity for vertices with no no paths to start vertex
     */
    public static <V> Map<V, Double> BellmanFord(Graph<V, ? extends Double> G, V start) {

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

    /**
     * Bellman-Ford algorithm for computing shortest paths from given vertex.
     * @param G Graph. Must implement edge labels as a numerical type.
     * @param start start vertex
     * @param end end vertex
     * @return Ordered list representing the shortest pathway from start vertex to end vertex.
     * Returns null if no path found.
     */
    public static <V> List<V> BellmanFordPath(Graph<V, ? extends Double> G, V start, V end) {

        /* initialize variables */
        int n = G.numVertices();
        List<V> vertices = (List<V>) G.vertices();
        Map<V, Double> costs = new HashMap<>();
        Map<V, V> backTrack = new HashMap<>();      // initialize backtrack
        costs.put(start, 0.0);

        /* initialize table */
        double[][] OPT = new double[n][n+1];

        /* save start vertex to back-track */
        backTrack.put(start, null);

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

                        /* update cost */
                        OPT[i][nextIndex] = OPT[i-1][index] + transition;
                        costs.remove(nextVertex);
                        costs.put(nextVertex, OPT[i][nextIndex]);

                        /* update backTrack */
                        backTrack.remove(nextVertex);
                        backTrack.put(nextVertex, currentVertex);
                    }
                }
            }
        }

        /* if cost of end vertex is infinity, no path eas found */
        if (costs.get(end) == Double.MAX_VALUE) {
            System.err.println("No path was found  from " + start + " to " + end + ".");
            return null;
        }

        /* rebuild path */
        Stack<V> path = new Stack<>();

        /* backTrack and prepend vertices to path */
        V vertex = end;
        while (vertex != null) {
            path.push(vertex);
            vertex = backTrack.get(vertex);
        }

        /* return the path */
        return path;
    }





    public static <V> void AStar(Graph <V, ? extends Double> G, V start, V end) {
//        Queue<V> toVisit = new PriorityQueue<V>(heuristic(G, v));
    }

    public static <V,E> @NotNull
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
//
//    private static <V> double heuristic(Graph<V,? extends Double> G, V v) {
//        int costs = G.inDegree(v) + G.outDegree(v);
//        double counts = 0.0;
//        for (V next : G.outNeighbors(v)) {
//            costs += G.getLabel(v, next);
//            count++;
//        }
//        for (V pred : G.inNeighbors(v)) {
//            costs += G.getLabel(pred, v);
//            count++;
//        }
//
//        return (costs / count);
//
//    }


}
