import entity.Node;
import service.LCR_ElectionService;
import service.RandomNodes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Collection<Node> nodes = RandomNodes.generateRandomNodes(10);
        System.out.println("Nodes generated:");
        for (Node node : nodes) {
            System.out.println(node);
        }


        System.out.println("Starting LCR Election Service...");
        LCR_ElectionService lcrElectionService = new LCR_ElectionService(nodes);
        lcrElectionService.startElection();

    }
}