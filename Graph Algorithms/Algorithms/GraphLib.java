import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * A library of Graph algorithms.
 * @author Amittai J. Wekesa (@siavava)
 */
public final class GraphLib {

    /**
     * Breadth-First Search to find predecessors of vertices in Graph
     * @param G: Graph
     * @param start start vertex
     * @param <V> vertex data type
     * @param <E> edge data type
     * @return Map containing vertices and their predecessors in bfs traversal.
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
     * Breadth-First Search with to find path A --> B
     * @param <V> vertex data type
     * @param <E> edge data type
     * @param G : Graph
     * @param start start vertex
     * @param end the end vertex
     * @return Either a String indicating that no path exists,
     * or a list of the vertices in the path from A to B in order of traversal.
     */
    public static <V,E> Object bfsPath(Graph<V,E> G, V start, V end) {
        System.out.println("BFS path-finding from " + start + " to " + end);

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
                    if (v.equals(end)) {
                        break;
                    }
                }
                if (v.equals(end)) {
                    break;
                }
            }
        }

        /* generate paths */
        V vertex = end;
        if (backTrack.getOrDefault(vertex, null) == null) {
            return "No connection from " + start + " to " + end;
        }
        List<V> path = new LinkedList<V>();
        for (; vertex != null; vertex = backTrack.getOrDefault(vertex, null)) {
            path.add(0, vertex);
        }
        return path;
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
        int time = 0;

        /* initialize variables */
        Map<V,V> backTrack = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Stack<V> stack = new Stack<>();

        backTrack.put(start, null);     // add start to backtrack

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
     * Depth-First Search
     * @param <V> vertex data type
     * @param <E> edge data type
     * @param G : Graph
     * @param start start vertex
     * @param end the end vertex
     * @return Either a String indicating that no path exists,
     * or a list of the vertices in the path from A to B in order of traversal.
     */
    public static <V,E> Object dfsPath(Graph<V,E> G, V start, V end) {
        System.out.println("Depth-First Search from " + start + " to " + end);

        /* initialize variables */
        Map<V,V> backTrack = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Stack<V> stack = new Stack<>();

        backTrack.put(start, null);     // add start to backtrack

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

        /* generate path */
        V vertex = end;
        if (backTrack.getOrDefault(vertex, null) == null) {
            return "No connection from " + start + " to " + end;
        }

        List<V> path = new LinkedList<V>();
        for (; vertex != null; vertex = backTrack.getOrDefault(vertex, null)) {
            path.add(0, vertex);
        }
        return path;
    }

    /**
     * Kahn's topological sorting algorithm
     * Returns a topological ordering of a Graph; that is,
     * an ordering with no backward dependencies.
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
        int size = copy.numVertices();              // check current number of vertices in Graph
        while(size > 0) {                           // while Graph still has vertices...
            int currentSize = size;
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
            if (size == currentSize) {
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
     * @param G : Graph implementing some vertex type and some edge type.
     *         Edge type must extend "Double" datatype (i.e. must be a number).
     * @return {vertex -> {adjacent vertex -> shortest path}}
     *          TODO: NOTE: We drop all infinity edges. If a vertex u does not
     *           have a minimum cost value to another vertex v in the Graph
     *           then u is unreachable from v.
     */
    public static <V,E> Map<V, Map<V, Integer>> FloydWarshallAPSP(Graph<V,E> G) {
        System.out.println("Running Floyd Warshall APSP on the Graph... \n");
        int n = G.numVertices();
        int[][][] OPT = new int[n+1][n+1][n+1];
        List<V> vertices = new ArrayList<>();
        G.vertices().forEach(vertices::add);

        /* initial step: calculate costs for direct connections */
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
                        OPT[i][j][0] = (int)G.getLabel(u, v);
                    }
                    else {
                        OPT[i][j][0] = Integer.MAX_VALUE;
                    }
                }
            }
        }

        /* Dynamic Programming Step */
        for (int k=1; k<=n; k++) {
            for (int i=1; i<=n; i++) {
                for (int j=1; j<=n; j++) {
                    if (OPT[i][j][k-1] == Integer.MAX_VALUE) {
                        if (OPT[i][k][k-1] == Integer.MAX_VALUE || OPT[k][j][k-1] == Integer.MAX_VALUE ) {
                            OPT[i][j][k] = Integer.MAX_VALUE;
                        }
                        else {
                            OPT[i][j][k] = OPT[i][k][k-1] + OPT[k][j][k-1];
                        }
                    }
                    else if (OPT[i][k][k-1] == Integer.MAX_VALUE || OPT[k][j][k-1] == Integer.MAX_VALUE ) {
                        OPT[i][j][k] = OPT[i][j][k-1];
                    }
                    else {
                        OPT[i][j][k] = Math.min(OPT[i][j][k-1], OPT[i][k][k-1] + OPT[k][j][k-1]);
                    }
                }
            }
        }

        /* Build Mapping of all costs */
        Map<V,Map<V,Integer>> costs = new HashMap<>();

        /* For each vertex */
        for (V u : G.vertices()) {
            int index = vertices.indexOf(u) + 1;
            for (V v : G.vertices()) {
                int nextIndex = vertices.indexOf(v) + 1;
                int cost = OPT[index][nextIndex][n];
                if (cost != Integer.MAX_VALUE) {
                    Map<V, Integer>currentCosts = costs.getOrDefault(u, new HashMap<>());
                    currentCosts.put(v, cost);
                    costs.put(u, currentCosts);
                }
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
        // get num of vertices in Graph
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
            int curr;
            if ( (curr = costs.get(current)) != Integer.MAX_VALUE) {
                for (V next : G.outNeighbors(current)) {
                    if (next != current) {
                        int currToNext = (int) G.getLabel(current, next);
                        if ((curr + currToNext < costs.get(next))) {
                            costs.put(next, (curr + currToNext));
                            queue.remove(next);
                            queue.add(next);
                            System.out.println(next + " : " + (curr + currToNext));
                        }
                    }
                }

            }
        }
        return costs;
    }

    /**
     * Dijkstra's algorithm for single-source shortest path.
     * Returns the shortest path from start vertex to end vertex.
     * To compute costs, use Dijkstra().
     * @param G : Graph
     * @param start : start vertex
     * @param end : The goal vertex
     * @return Map of costs of vertices from start
     */
    public static <V,E> Object DijkstraPath(Graph<V,E> G, V start, V end) {
        System.out.println("Dijkstra Pathfinding from '" + start + "' to '" + end + "'." );
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
            /* extract min from queue */
            V current = queue.remove();

            /* get score of current */
            int curr;
            if ( ( curr = costs.get(current)) != Integer.MAX_VALUE) {
                /* for each adjacent vertex, update cost if necessary */
                for (V next : G.outNeighbors(current)) {
                    if (next != current) {
                        int currToNext = (int) G.getLabel(current, next);
                        if ((curr + currToNext < costs.get(next))) {
                            costs.put(next, (curr + currToNext));
                            queue.remove(next);
                            queue.add(next);

                            /* if update done, remember the back-pointer */
                            backTrack.put(next, current);
                        }
                    }
                }
                if (current == end) {
                    break;
                }
                else if (costs.get(current) == Integer.MAX_VALUE) {
                    return "not found.";
                }
            }

        }

        /* rebuild path */
        List<V> path = new LinkedList<>();

        /* backTrack and prepend vertices to path */
//        V vertex = end;
        for (V vertex=end; vertex != null; vertex=backTrack.get(vertex)) {
            path.add(0, vertex);
        }

        /* return reconstructed path */
        return path;
    }

    /**
     * Bellman-Ford algorithm for computing shortest path costs from given vertex.
     * @param G Graph. Must implement edge labels as a numerical type.
     * @param start start vertex
     * @return Map of all vertices in the Graph and their distance from start vertex.
     * Returns infinity for vertices with no no paths to start vertex
     */
    public static <V,E> Map<V, Integer> BellmanFord(Graph<V,E> G, V start) {

        System.out.println("Running BellmanFord on the Graph from '" + start + "'.");

        /* initialize variables */
        int n = G.numVertices();
        List<V> vertices = new ArrayList<>();
        G.vertices().forEach(vertices::add);
        Map<V, Integer> costs = new HashMap<>();
        costs.put(start, 0);

        /* initialize table */
        int[][] OPT = new int[n][n+1];

        /* initialize zero-th iteration */
        for (int i=0; i<=n; i++) {
            OPT[0][i] = Integer.MAX_VALUE;
        }

        /* start vertex to itself is 0 */
        OPT[0][vertices.indexOf(start)+1] = 0;

        /* loop 1 to n-1 times */
        for (int i=1; i<n; i++) {

            for (V currentVertex : G.vertices()) {                              // for each vertex...
                int currentIndex = vertices.indexOf(currentVertex) + 1;
                OPT[i][currentIndex] = OPT[i-1][currentIndex];                   // get value from previous iteration
            }
            for (V currentVertex : G.vertices()) {                              // for each vertex...
                int currentIndex = vertices.indexOf(currentVertex) + 1;
                for (V nextVertex : G.outNeighbors(currentVertex)) {             // get outbound neighbors
                    int nextIndex = vertices.indexOf(nextVertex) + 1;
                    int transition = (int) G.getLabel(currentVertex, nextVertex);

                    /*
                     * if path from current vertex improves min cost to neighbor,
                     * perform the improvement and save the new cost
                     */

                    if ( (OPT[i-1][currentIndex] != Integer.MAX_VALUE) &&
                            (OPT[i-1][currentIndex]) + transition < OPT[i][nextIndex]) {
                        OPT[i][nextIndex] = OPT[i-1][currentIndex] + transition;
                        costs.put(nextVertex, OPT[i][nextIndex]);
                        System.out.println(nextVertex + " : " + OPT[i][nextIndex]);
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
     * @return Ordered list representing the shortest pathway from start vertex to end vertex.
     * Returns null if no path found.
     */
    public static <V,E> Map<V, List<V>> BellmanFordSSSP(Graph<V,E> G, V start) {

        System.out.println("Finding all shortest paths from '" + start + "' using BellmanFord SSSP.");

        /* initialize variables */
        int n = G.numVertices();
        List<V> vertices = new ArrayList<>();
        G.vertices().forEach(vertices::add);
        Map<V, V> backTrack = new HashMap<>();      // initialize backtrack

        /* initialize table */
        int[][] OPT = new int[n][n+1];

        /* save start vertex to back-track */
        backTrack.put(start, null);

        /* initialize zero-th iteration */
        for (int i=1; i<=n; i++) {
            OPT[0][i] = Integer.MAX_VALUE;
        }

        /* start vertex to itself is 0 */
        OPT[0][vertices.indexOf(start)+1] = 0;

        /* loop 1 to n-1 times */
        for (int i=1; i<n; i++) {

            for (V currentVertex : G.vertices()) {                              // for each vertex...
                int index = vertices.indexOf(currentVertex) + 1;
                OPT[i][index] = OPT[i-1][index];                                // get value from previous iteration
            }
            for (V currentVertex : G.vertices()) {                              // for each vertex...
                int currentIndex = vertices.indexOf(currentVertex) + 1;
                for (V nextVertex : G.outNeighbors(currentVertex)) {             // get outbound neighbors
                    int nextIndex = vertices.indexOf(nextVertex) + 1;
                    int transition = (int) G.getLabel(currentVertex, nextVertex);

                    /*
                     * if path from current vertex improves min cost to neighbor,
                     * perform the improvement and save the new cost
                     */

                    if ( (OPT[i-1][currentIndex] != Integer.MAX_VALUE) &&
                            (OPT[i-1][currentIndex]) + transition < OPT[i][nextIndex]) {

                        /* update cost */
                        OPT[i][nextIndex] = OPT[i-1][currentIndex] + transition;

                        /* update backTrack */
                        backTrack.put(nextVertex, currentVertex);
                    }
                }
            }
        }

        /* rebuild path */
        Map<V, List<V>> paths = new HashMap<>();

        for (V v : G.vertices()) {
            /* backTrack and prepend vertices to path */
            List<V> path = new LinkedList<>();
            V vertex = v;
            if (vertex.equals(start)) {
                path.add((V) "start vertex");
            }
            /* if immediate predecessor is null, no path exists */
            else if (backTrack.get(vertex) == null) {
                path.add((V) "no path");
            }
            else {
                while (vertex != null) {
                    path.add(0, vertex);
                    vertex = backTrack.get(vertex);
                }
            }
            paths.put(v, path);
        }


        /* return the Dictionary of shortest paths */
        return paths;
    }

    /**
     * Dijkstra's algorithm for single-source shortest paths.
     * Returns the shortest path from start vertex to every other vertex in Graph.
     * To compute costs, use Dijkstra().
     * @param G : Graph
     * @param start : start vertex
     * @param end : The goal vertex
     * @return Map of costs of vertices from start
     */
    public static <V,E> Object AStar(Graph<V,E> G, V start, V end) {
        System.out.println("A* Pathfinding from '" + start + "' to '" + end + "'." );
        Map<V,Integer> popularities = G.getPopularities();
        Map<V, Integer> costs = new HashMap<>();     // initialize map of costs
        Map<V, V> backTrack = new HashMap<>();      // initialize backtrack

        /* save start vertex to back-track */
        backTrack.put(start, null);

        /* Initialize the priority queue */
//        Queue<V> queue = new PriorityQueue<>(G.numVertices(), Comparator.comparingInt(costs::get + popularities::get));

        Queue<V> queue = new PriorityQueue<>(G.numVertices(), (n1, n2) -> {
            // compare n1 and n2
            int one = costs.get(n1) + popularities.get(n1) / 2;
            int two = costs.get(n2) + popularities.get(n2) / 2;
//            System.out.println("one: " + one + " two: " + two);

            return Integer.compare(two, one);
        });

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
            /* extract min from queue */
            V current = queue.remove();

            /* get score of current */
            int curr = costs.get(current);

            /* for each adjacent vertex, update cost if necessary */
            for (V next : G.outNeighbors(current)) {
                if (next != current) {
                    int currToNext = (int) G.getLabel(current, next);
                    if ((curr + currToNext < costs.get(next))) {
                        costs.put(next, (curr + currToNext));
                        queue.remove(next);
                        queue.add(next);

                        /* if update done, remember the back-pointer */
                        backTrack.put(next, current);
                    }

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

        /* if predecessor of end vertex is null,
           no path exists. */
        if (backTrack.getOrDefault(end, null) == null) {
            return "No path exists from " + start + " to " + end + ".";
        }

        /* backTrack and prepend vertices to path */
        for (V vertex=end; vertex != null; vertex=backTrack.get(vertex)) {
            path.add(0, vertex);
        }

        /* return reconstructed path */
        return path;
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
}
