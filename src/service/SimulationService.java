package service;

import entity.common.MessageLog;
import entity.common.Node;
import entity.common.NodeLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static entity.common.Port.LEFT;
import static entity.common.Port.RIGHT;

public abstract class SimulationService {
    protected Collection<Node> nodes = new ArrayList<>();
    protected final List<NodeLog> nodeLogs = new ArrayList<>();
    protected final List<MessageLog> messageLogs = new ArrayList<>();

    public void startSimulation() throws CloneNotSupportedException {
        System.out.println("Starting HS election simulation...");
        boolean allAcknowledgeLeaders = false;
        int round = 0;
        int totalMsg = 0;

        while (!allAcknowledgeLeaders) {
            round++;
            System.out.println("\nStarting Round " + round);

            allAcknowledgeLeaders = true;

            for (Node node : nodes) {
                node.readBuffer();
            }

            for (Node node : nodes) {
                if (round == 1) {
                    node.start();
                } else {
                    node.processMessages();
                }
                if (node.getLeaderId() == null) {
                    allAcknowledgeLeaders = false;
                }

                nodeLogs.add(new NodeLog(round, (Node) node.clone()));
                if (node.getLastSentMessage().get(LEFT) != null) {
                    totalMsg++;
                    messageLogs.add(new MessageLog(round, node.getId(), node.getNeighbors().get(LEFT).getId(), node.getLastSentMessage().get(LEFT)));
                }
                if (node.getLastSentMessage().get(RIGHT) != null) {
                    totalMsg++;
                    messageLogs.add(new MessageLog(round, node.getId(), node.getNeighbors().get(RIGHT).getId(), node.getLastSentMessage().get(RIGHT)));
                }
            }
        }

        System.out.println("\nElection completed in " + round + " rounds with " + totalMsg + " messages sent.");
    }
}
