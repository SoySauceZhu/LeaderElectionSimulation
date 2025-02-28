package service;

import entity.common.MessageLog;
import entity.common.Node;
import entity.common.NodeLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SimulationService {
    protected Collection<Node> nodes = new ArrayList<>();
    protected final List<List<NodeLog>> nodeLog = new ArrayList<>();
    protected final List<MessageLog> messageLogs = new ArrayList<>();

    public void startSimulation() {
        System.out.println("Starting HS election simulation...");
        boolean allAcknowledgeLeaders = false;
        int round = 0;
        int totalMsg = 0;

        while (!allAcknowledgeLeaders) {
            round++;
            System.out.println("\nStarting Round " + round);

            List<NodeLog> roundNodeLog = new ArrayList<>();
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
            }
        }

        System.out.println("\nElection completed in " + round + " rounds with " + totalMsg + " messages sent.");
    }
}
