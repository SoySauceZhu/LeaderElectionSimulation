package service;


import entity.Node;
import entity.Status;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static util.Util.printInBox;

public class LCR_ElectionService<T> {

    Collection<Node> nodes;

    public LCR_ElectionService(Collection<Node> nodes) {
        this.nodes = nodes;
    }

    public void startElection() {

        System.out.println("Starting the election process...");
        boolean allTerminated = false;
        int round = 0;

        for (Node node : nodes) {
            node.start();
        }

        while (!allTerminated) {
            System.out.println("\n\n");
            printInBox("Round " + (++round));

            boolean allAcknoledgeLeaders = true;

            for (Node node : nodes) {
                node.readBuffer();
            }

            for (Node node : nodes) {
                node.processMessages();
                if (node.getLeaderId() == null) {
                    allAcknoledgeLeaders = false;
                }
            }

            if (allAcknoledgeLeaders) {
                break;
            }

        }

        Node[] nodesArray = nodes.toArray(new Node[0]);

        System.out.println("Election process completed.");
        System.out.println("Leader is Node " + nodesArray[0].getLeaderId());
        for (Node node : nodes) {
            System.out.println(node);
        }


    }
}
