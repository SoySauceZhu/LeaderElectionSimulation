package service;

import entity.common.MessageLog;
import entity.common.NodeState;
import entity.hs.Message;
import entity.hs.Node;
import entity.hs.Port;

import java.util.*;

public class HS_SimulationService implements SimulationService {
    private final Collection<Node> nodes;
    private final List<Map<Integer, NodeState>> nodeStatusHistory = new ArrayList<>();
    private final List<MessageLog> messageLogs = new ArrayList<>();

    public HS_SimulationService(Collection<Node> nodes) {
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
                boolean sent;
                if (round == 1) {
                    sent = node.start();
                } else {
                    sent = node.processMessages();
                }
                if (sent) {
                    Message leftPortLasMsg = node.getLastSentMessage().get(Port.LEFT);
                    Message rightPortLastMsg = node.getLastSentMessage().get(Port.RIGHT);
                    if (leftPortLasMsg != null) {
                        Message lastMessage = leftPortLasMsg;
                        messageLogs.add(new MessageLog(round, node.getId(), node.getNeighbors().get(Port.RIGHT).getId(), lastMessage.getMsgType(), lastMessage.getId()));
                        totalMsg++;
                    }
                    if (rightPortLastMsg != null) {
                        Message lastMessage = rightPortLastMsg;
                        messageLogs.add(new MessageLog(round, node.getId(), node.getNeighbors().get(Port.LEFT).getId(), lastMessage.getMsgType(), lastMessage.getId()));
                        totalMsg++;
                    }
                }
                if (node.getLeaderId() == null) {
                    allAcknowledgeLeaders = false;
                }

                currentRoundState.put(node.getId(), new NodeState(node.getId(), node.getPhase(), node.getStatus(), node.getLeaderId(), node.isTerminated()));
            }

            nodeStatusHistory.add(currentRoundState);

            if (allAcknowledgeLeaders) {
                allTerminated = true;
            }
        }

        System.out.println("\nElection completed in " + round + " rounds with " + totalMsg + " messages sent.");
    }



    public static List<Node> generateNodeRing(String digits) {
        List<Node> nodes = new ArrayList<>();

        // Create nodes from digits
        for (char digit : digits.toCharArray()) {
            nodes.add(new Node(Character.getNumericValue(digit)));
        }

        // Link nodes in a ring
        for (int i = 0; i < nodes.size(); i++) {
            Node currentNode = nodes.get(i);
            Node nextNode = nodes.get((i + 1) % nodes.size());
            Node prevNode = nodes.get((i - 1 + nodes.size()) % nodes.size());
            currentNode.setNeighbor(Port.RIGHT, nextNode);
            currentNode.setNeighbor(Port.LEFT, prevNode);
        }

        return nodes;
    }

    public static void main(String[] args) {
        List<Node> nodes = generateNodeRing("0123456789");

        SimulationService service = new HS_SimulationService(nodes);



        System.out.println("Nodes generated:");
        for (Node node : nodes) {
            System.out.println(node);
        }

        System.out.println("\n\n");

        System.out.println("Starting LCR Election Service...");
        service.startSimulation();

        for (Node node : nodes) {
            System.out.println(node);
        }
    }

}
