package view;

import entity.MessageLog;
import entity.NodeState;
import service.LCR_SimulationService;

import java.util.List;
import java.util.Map;

public class View {
    private final LCR_SimulationService simulationService;

    public View(LCR_SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public void displayNodeStatusHistory() {
        List<Map<Integer, NodeState>> history = simulationService.getNodeStatusHistory();
        System.out.println("\nNode Status History:");
        for (int round = 0; round < history.size(); round++) {
            System.out.println("Round " + (round + 1) + ":");
            for (Map.Entry<Integer, NodeState> entry : history.get(round).entrySet()) {
                System.out.println("  Node " + entry.getKey() + " -> " + entry.getValue());
            }
            System.out.println();
        }
    }

    public void displayMessageLogs() {
        List<MessageLog> logs = simulationService.getMessageLogs();
        System.out.println("\nMessage Transmission Log:");
        for (MessageLog log : logs) {
            System.out.println("  Round " + log.getRound() + ": Node " + log.getSender() + " -> Node " + log.getReceiver() + " | Type: " + log.getMessageType() + " | Content: " + log.getMessageContent());
        }
    }
}
