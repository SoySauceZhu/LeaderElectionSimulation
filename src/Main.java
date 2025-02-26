import entity.Node;
import service.LCR_ElectionService;
import service.RandomNodes;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        List<Node> nodes = (List<Node>) RandomNodes.generateNodes(10);
        System.out.println("Nodes generated:");
        for (Node node : nodes) {
            System.out.println(node);
        }


        System.out.println("Starting LCR Election Service...");
        LCR_ElectionService lcrElectionService = new LCR_ElectionService(nodes);
        lcrElectionService.startElection();

    }
}