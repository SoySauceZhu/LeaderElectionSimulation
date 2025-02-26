import entity.Node;
import entity.Status;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Node> nodes = new ArrayList<>();
        int[] nodeIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int id : nodeIds) {
            nodes.add(new Node(id));
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Node nextNode = nodes.get((i + 1) % nodes.size());
            node.setNext(nextNode);
        }

        System.out.println("Starting the election process...");
        boolean allTerminated = false;
        int round = 0;

        for (Node node : nodes) {
            node.start();
        }

        while (!allTerminated) {
            System.out.println("Round " + (++round));

            for (Node node : nodes) {
                node.processMessages();
                if (node.getStatus() == Status.LEADER) {
                    allTerminated = true;
                }
            }

        }

        System.out.println("Election process completed.");

    }
}
