import java.util.*;

public class GraphTest {
    public static void main(String[] args) {
        Graph<String, Integer> network = new AdjacencyMapGraph<>();
//        for (char letter='A'; letter<='H'; letter++) {
//            network.insertVertex(String.valueOf(letter));
//        }

        Random random = new Random();
        network.insertVertex("A");
        network.insertVertex("Z");
        for (int i=0; i<10; i++) {
            network.insertVertex(String.valueOf((char)('A' + random.nextInt(25))));
        }

        for (int i=0; i<150; i++) {
            String u = String.valueOf((char) ('A' + random.nextInt(26)));
            String v = String.valueOf((char)('A' + random.nextInt(26)));
            if (network.hasVertex(u) && network.hasVertex(v)) {
                if (!u.equals(v)) {
                    network.insertDirected(u, v, Math.max(1, random.nextInt(10)));
                }
                if (i % 2 == 0 && !u.equals("Z")) {
                    network.insertDirected(u, "Z", Math.max(1, random.nextInt(10)));
                }
                else if (!v.equals("A")){
                    network.insertDirected("A", v, Math.max(1, random.nextInt(10)));
                }
            }
        }
        network.removeDirected("A", "Z");

        System.out.println(network);

        System.out.println(GraphLib.bfs(network, "A"));

        System.out.println(GraphLib.dfs(network, "A"));

//        for (int i=0; i<3; i++) {
//            for (String v1 : network.vertices()) {
//                for (String v2 : network.vertices()) {
//                    if (!v2.equals(v1)) {
//                        System.out.println(GraphLib.Dijkstra(network, v1));
//                        System.out.println(GraphLib.DijkstraPath(network, v1, v2));
//                        System.out.println(GraphLib.BellmanFord(network, "A"));
////                        System.out.println(GraphLib.AStar(network, v1, v2));
//                        System.out.println(GraphLib.Dijkstra(network, v2));
//                        System.out.println(GraphLib.DijkstraPath(network, v2, v1));
////                        System.out.println(GraphLib.AStar(network, v2, v1));
//                        break;
//                    }
//                }
//            }
//        }
        System.out.println(GraphLib.BellmanFord(network, "A"));


        Graph<String, Integer> test = new AdjacencyMapGraph<>();
        test.insertVertex("A");
        test.insertVertex("B");
        test.insertVertex("C");
        test.insertVertex("D");
        test.insertVertex("E");
        test.insertVertex("F");

        test.insertDirected("A", "F", 7);
        test.insertDirected("B", "E", 6);
        test.insertDirected("C", "D", 8);
        test.insertDirected("B", "D", 5);
        test.insertDirected("C", "E", 1);
        test.insertDirected("A", "C", 1);
        test.insertDirected("B", "C", 1);

        System.out.println(GraphLib.TopoSort(test));






//        Graph<String, HashSet<String>> movies = new AdjacencyMapGraph<>();
//        // Add vertices
//        movies.insertVertex("Kevin Bacon");
//        movies.insertVertex("Bob");
//        movies.insertVertex("Alice");
//        movies.insertVertex("Charlie");
//        movies.insertVertex("Dartmouth (Earl thereof)");
//        movies.insertVertex("Nobody");
//        movies.insertVertex("Nobody's Friend");
//
//        // Add undirected connections
//        movies.insertUndirected("Kevin Bacon", "Bob", new HashSet<>(){{add("A Movie");}});
//        movies.insertUndirected("Kevin Bacon", "Alice", new HashSet<>(){{add("A Movie"); add("E Movie");}});
//        movies.insertUndirected("Bob", "Alice", new HashSet<>(){{add("A Movie");}});
//        movies.insertUndirected("Bob", "Charlie", new HashSet<>(){{add("C Movie");}});
//        movies.insertUndirected("Alice", "Charlie", new HashSet<>(){{add("D Movie");}});
//        movies.insertUndirected("Charlie", "Dartmouth (Earl thereof)", new HashSet<>(){{add("B Movie");}});
//        movies.insertUndirected("Nobody", "Nobody's Friend", new HashSet<>(){{add("F Movie");}});
//
//        System.out.println(movies);
//
////        Graph<String, HashSet<String>> shortestPaths = GraphLib.bfs(movies, "Kevin Bacon");
////        System.out.println(shortestPaths);
////        System.out.println(GraphLib.getPath(shortestPaths, "Dartmouth (Earl thereof)"));
//////        System.out.println(movies.outNeighbors("Charlie"));
////        System.out.println("Shortest path Alice to Kevin Bacon: " + GraphLib.getPath(shortestPaths, "Alice"));
//        System.out.println("Full Graph: " + movies.vertices());
//        System.out.println("Sub Graph:" + shortestPaths.vertices());
//        System.out.println("Missing: " + GraphLib.missingVertices(movies, shortestPaths));
//
//        System.out.println("Average Separation: " + GraphLib.averageSeparation(shortestPaths, "Kevin Bacon"));
    }

}
