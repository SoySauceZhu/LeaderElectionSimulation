package service;


import entity.Node;

import java.util.Arrays;
import java.util.Collection;

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
        int totalMsg = 0;
        int roundMsg = 0;

        System.out.println("\n\n");
        printInBox("Round " + (++round) + " starting round");
        for (Node node : nodes) {
            node.start();
            roundMsg++;
            totalMsg++;
        }
        System.out.println("\nRound " + round + " completed. Total messages sent in this round: " + roundMsg);

        while (!allTerminated) {
            System.out.println("\n\n");
            printInBox("Round " + (++round));

            boolean allAcknoledgeLeaders = true;

            for (Node node : nodes) {
                node.readBuffer();
            }

            for (Node node : nodes) {
                boolean sent = node.processMessages();
                if (sent) {
                    roundMsg++;
                    totalMsg++;
                }
                if (node.getLeaderId() == null) {
                    allAcknoledgeLeaders = false;
                }
            }

            System.out.println("\nRound " + round + " completed. Total messages sent in this round: " + roundMsg);

            if (allAcknoledgeLeaders) {
                break;
            }

        }

        Node[] nodesArray = nodes.toArray(new Node[0]);
        Arrays.stream(nodesArray).sorted();


        System.out.println("\n\n");
        printInBox("Election process completed.");
        System.out.printf("Total rounds: %d\n", round);
        System.out.println("Total messages sent: " + totalMsg);
        System.out.println("Leader is Node " + nodesArray[0].getLeaderId());
        System.out.println("\n\n");
        printInBox("Nodes status");
        for (Node node : nodes) {
            System.out.println(node);
        }


    }
}