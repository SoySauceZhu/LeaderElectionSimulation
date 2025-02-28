package service;

import entity.lcr.Message;
import entity.common.MessageLog;
import entity.lcr.Node;
import entity.common.NodeState;

import java.util.*;

public class LCR_SimulationService {
    private final Collection<Node> nodes;
    private final List<Map<Integer, NodeState>> nodeStatusHistory = new ArrayList<>();
    private final List<MessageLog> messageLogs = new ArrayList<>();

    public LCR_SimulationService(Collection<Node> nodes) {
        this.nodes = nodes;
    }

    public void startSimulation() {
        System.out.println("Starting the election simulation...");
        boolean allTerminated = false;
        int round = 0;
        int totalMsg = 0;

        while (!allTerminated) {
            round++;
            System.out.println("\nStarting Round " + round);

            Map<Integer, NodeState> currentRoundState = new HashMap<>();
            boolean allAcknowledgeLeaders = true;

            for (Node node : nodes) {
                node.readBuffer();
            }

            for (Node node : nodes) {
                boolean sent = false;
                if (round == 1) {
                    sent = node.start();
                } else {
                    sent = node.processMessages();
                }
                if (sent) {
                    Message lastMessage = node.getLastSentMessage();
                    if (lastMessage != null) {
                        messageLogs.add(new MessageLog(round, node.getId(), node.getNext().getId(), lastMessage.getMsgType(), lastMessage.getMsgContent()));
                        totalMsg++;
                    }
                }
                if (node.getLeaderId() == null) {
                    allAcknowledgeLeaders = false;
                }

                currentRoundState.put(node.getId(), new NodeState(node.getId(), node.getCurMaxId(), node.getStatus(), node.getLeaderId(), node.isTerminated()));
            }

            nodeStatusHistory.add(currentRoundState);

            if (allAcknowledgeLeaders) {
                allTerminated = true;
            }
        }

        System.out.println("\nElection completed in " + round + " rounds with " + totalMsg + " messages sent.");
    }

    public List<Map<Integer, NodeState>> getNodeStatusHistory() {
        return nodeStatusHistory;
    }

    public List<MessageLog> getMessageLogs() {
        return messageLogs;
    }
}


