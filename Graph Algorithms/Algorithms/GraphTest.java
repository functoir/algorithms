import java.util.*;

public class GraphTest {
    public static void main(String[] args) {

        /* random number generator */
        Random random = new Random();

        /* create a new Graph instance */
        Graph<String, Integer> network = new AdjacencyMapGraph<>();

        /* populate Graph with vertices */
        network.insertVertex("A");
        network.insertVertex("Z");
        for (int i=0; i<10; i++) {
            network.insertVertex(String.valueOf((char)('A' + random.nextInt(25))));
        }


        /* populate Graph with random edges*/
        for (int i=0; i<150; i++) {
            String u = String.valueOf((char) ('A' + random.nextInt(26)));
            String v = String.valueOf((char) ('A' + random.nextInt(26)));
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

        /* remove directed edge from A to Z
           to make results interesting */
        network.removeDirected("A", "Z");

        /* print the generated Graph */
        System.out.println(network);


        // TODO: Unmute to run more extensive tests on the Graph

        /* run traversals from random vertices in Graph */
        for (int i=0; i<3; i++) {
            for (String v1 : network.vertices()) {
                System.out.println(GraphLib.bfs(network, v1) + "\n\n");
                System.out.println(GraphLib.dfs(network, v1) + "\n\n");
                System.out.println(GraphLib.Dijkstra(network, v1) + "\n\n");
                System.out.println(GraphLib.BellmanFord(network, v1) + "\n\n");
                System.out.println(GraphLib.BellmanFordSSSP(network, v1) + "\n\n");


                for (String v2 : network.vertices()) {
                    if (!v2.equals(v1)) {
                        System.out.println(GraphLib.DijkstraPath(network, v1, v2));
                        break;
                    }
                }
            }
        }
        System.out.println(GraphLib.FloydWarshallAPSP(network) + "\n\n");
        System.out.println(network);



        /* I created a new Graph that can be topologically sorted
           because         -- most --         of the Graphs
           generated above turn out to be cyclic */
        Graph<String, Integer> sortableGraph = new AdjacencyMapGraph<>();
        sortableGraph.insertVertex("Task 1");
        sortableGraph.insertVertex("Task 2");
        sortableGraph.insertVertex("Task 3");
        sortableGraph.insertVertex("Task 4");
        sortableGraph.insertVertex("Task 5");
        sortableGraph.insertVertex("Task 6");

        sortableGraph.insertDirected("Task 1", "Task 6", 7);
        sortableGraph.insertDirected("Task 2", "Task 5", 6);
        sortableGraph.insertDirected("Task 3", "Task 4", 8);
        sortableGraph.insertDirected("Task 4", "Task 2", 5);
        sortableGraph.insertDirected("Task 1", "Task 5", 1);
        sortableGraph.insertDirected("Task 1", "Task 4", 1);
        sortableGraph.insertDirected("Task 6", "Task 5", 1);

        System.out.println("Running Topo-Sort on new Graph");
        System.out.println(sortableGraph);
        System.out.println("Topological Ordering: " + GraphLib.TopoSort(sortableGraph));
    }

}
