import entity.common.Node;
import entity.common.NodeType;
import service.SimulationService;
import util.PrintBox;

import java.util.Collection;

public class Stat {
    private long runtime;
    private int messageCount;
    private long memoryUsage;
    private int leaderCount;

    public void runSimulation(Collection<Node> nodes, boolean msgLog, boolean nodeLog) throws CloneNotSupportedException {
        SimulationService service = new SimulationService();
        service.setNodes(nodes);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        service.startSimulation(msgLog, nodeLog);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterUsedMem - beforeUsedMem;

        this.runtime = duration;
        this.memoryUsage = memoryUsed;
        this.messageCount = service.getTotalMessages();
        this.leaderCount = numberOfLeader(nodes);
    }

    private int numberOfLeader(Collection<Node> nodes) {
        int leaderNum = 0;
        for (Node node : nodes) {
            if (node.getNodeType().equals(NodeType.LEADER)) {
                leaderNum++;
            }
        }
        return leaderNum;
    }

    public void printStats() {
        PrintBox.printInBox("Simulation Statistics");
        System.out.println("Execution time: " + (double) runtime / 1_000_000 + " milliseconds");
        System.out.println("Memory used: " + memoryUsage + " bytes");
        System.out.println("Number of messages: " + messageCount);
        System.out.println("Number of leaders: " + leaderCount);
    }

    public long getRuntime() {
        return runtime;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public int getLeaderCount() {
        return leaderCount;
    }
}