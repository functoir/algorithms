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


        // TODO: Unmute to run more extensive tests on the Graph
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



        /* I created a Graph that can be sorted because
        -- most --
         of the Graphs generated above turn out to be cyclic */
        Graph<String, Integer> test = new AdjacencyMapGraph<>();
        test.insertVertex("Task 1");
        test.insertVertex("Task 2");
        test.insertVertex("Task 3");
        test.insertVertex("Task 4");
        test.insertVertex("Task 5");
        test.insertVertex("Task 6");

        test.insertDirected("Task 1", "Task 6", 7);
        test.insertDirected("Task 2", "Task 5", 6);
        test.insertDirected("Task 3", "Task 4", 8);
        test.insertDirected("Task 4", "Task 2", 5);
        test.insertDirected("Task 1", "Task 5", 1);
        test.insertDirected("Task 1", "Task 4", 1);
        test.insertDirected("Task 6", "Task 5", 1);

        System.out.println("Running Topo-Sort on new Graph");
        System.out.println(test);
        System.out.println("Topological Ordering: " + GraphLib.TopoSort(test));
    }

}
