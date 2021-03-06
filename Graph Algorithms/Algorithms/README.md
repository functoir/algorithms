# GraphLib
Author: Amittai J. Wekesa (github: @siavava)

We implement a library of [Graph](https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)) 
algorithms that can be
applied to members of the [Graph](../Data%20Structures/Graph.java) interface types such
as the implemented [AdjacencyMapGraph](../Data%20Structures/AdjacencyMapGraph.java).

Each algorithm is documented in its implementation. Here's a general summary of 
everything implemented.

***

### bfs : 
An implementation of [Breadth-First Search](https://en.wikipedia.org/wiki/Breadth-first_search) using a 
[Linked List](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html) as a 
FIFO queue of vertices to visit. 

* Inputs: a `Graph` and a `start vertex`
* Returns: a `Map` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  for type description) of all vertices
  in the Graph that have a connection to the start vertex, and their
  respective costs from the start vertex.
  
***

### bfsPath :
An implementation of [Breadth-First Search](https://en.wikipedia.org/wiki/Breadth-first_search)
using a [Linked List](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html) as a
FIFO queue of vertices to visit (same as above).

* Inputs: a `Graph`, a `start vertex`, and an `end vertex`.
* Returns: a `List` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/List.html) for
  type description) of a bfs path 
  from start vertex to end vertex, or a `String` (see [here](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html)
  for type description) indicating that no such
  path exists in the Graph.
  
  **NOTE: Breadth-First Search guarantees a shortest path in terms
  of number of edges.**

***

### dfs :
An implementation of [Depth-First Search](https://en.wikipedia.org/wiki/Depth-first_search)
using a [Stack](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Stack.html)
as a LIFO queue of vertices to visit.

* Inputs: a `Graph` and a `start vertex`
* Returns: a `Map` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  for type description) of all vertices
  in the Graph that have a connection to the start vertex, and their
  respective costs from the start vertex.

***

### dfsPath :
An implementation of [Depth-First Search](https://en.wikipedia.org/wiki/Depth-first_search)
using a [Stack](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Stack.html)
as a LIFO queue of vertices to visit (same as above).

* Inputs: a `Graph`, a `start vertex`, and an `end vertex`.
* Returns: a `List` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/List.html) for
  type description) of a dfs path
  from start vertex to end vertex, or a `String` indicating that no such 
  path exists in the Graph.
  

  **NOTE: Depth-First Search does not guarantee a shortest path
  in terms number of edges.** 
  
***

### TopoSort :
An implementation of [Kahn's topological sorting algorithm]() to find a topological
ordering of the vertices in a Graph (that is, an orderin with no backward dependencies).

* Inputs: a `Graph`
* Outputs: a `Queue` (see [here]() for type description) of ordered vertices with no
  back dependencies, or a `String` indicating that no such
  ordering exists in the Graph.
  
***

### FloydWarshallAPSP :
An implementation of Floyd Warshall's All-Pairs Shortest-Paths algorithm
using 2D integer array `int[][]` as a table of values and a `Map` (see [here](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Map.html) for type description)
to hold mappings of each vertex to every other *reachable* vertex and its Shortest
distance away from the vertex.
* Inputs: a `Graph`
* Returns: a `Map` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  for type description); {vertex `u` -> { reachable vertices `v` -> cost of `v` from `u` } }
for every vertex `u` in `G` and every vertex `v` reachable from `u`.

** Example: We use FloydWarshallAPSP to construct shortest-path dictionary for A-Star Pathfinding.
To make it more efficient, we construct a dictionary of the shortest paths into the Graph, so it has to only be build once, Either
at construction or when it's polled (if a change has happened). **


***

### Dijkstra :
An implementation of Dijkstra's famous path-finding algorithm for weighted directed Graphs with no negative edge weights.
We use a `PriorityQueue` (see [here]() for more details) to implement a best-first ordering of edges in a Graph.

* Inputs: a `Graph`, and a `start vertex`.
* Returns: a `Map` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  for type description) of every other vertex and its shortest path from start vertex.
 ***

### DijkstraPath :
An implementation of Dijkstra's famous path-finding algorithm for weighted directed Graphs with no negative edge weights.
We use a `PriorityQueue` (see [here]() for more details) to implement a best-first ordering of edges in a Graph.

* Inputs: a `Graph`, a `start vertex`, and an `end vertex`.
* Returns: either an ordered `List` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/List.html) for
  type description) of every vertex in the path from start vertex to end vertex, or a `String` vertex 
  indicating that no such vertex exists. The caller should plan to handle either case.
  
***

### BellmanFord :
An implementation of BellmanFord's algorithm for single-source shortest paths.
This version returns a `Map` (see [here](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Map.html) for type description) of every reachable vertex in the Graph and its *cost* from the start vertex.

* Inputs: a `Graph`, and a `start vertex`.
* Returns: a `Map` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  for type description) of every reachable vertex in the Graph and its *cost* from the start vertex.

***

### BellmanFordSSSP :
An implementation of BellmanFord's algorithm for single-source shortest paths.
This version returns a `Map` (see here fore more details) of every reachable vertex in the Graph and its *shortest path* from the start vertex.

* Inputs: a `Graph`, and a `start vertex`.
* Returns: a `Map` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  for type description) of every reachable vertex in the Graph and its *shortest path* from the start vertex.

***
### AStar :
An implementation of [A* Search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm) for optimized Graph path-finding. At it's core, A* is a modified version 
of Dijkstra's algorithm with an added look-ahead heuristic.
This particular version expects the input `Graph` (see [here](https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)) for more details)
to have a "shortest path length" index of its vertices.
This is built using FloydWarshallAPSP. The latter is expensive, but it needs to be built only once for any Graph.

* Inputs: a `Graph`, a `start vertex`, and an `end vertex`.
* Returns: either an ordered `List` (see [here](https://docs.oracle.com/javase/8/docs/api/java/util/List.html) for
  type description) of every vertex in the path from start vertex to end vertex, or a `String` vertex
  indicating that no such vertex exists. The caller should plan to handle either case.