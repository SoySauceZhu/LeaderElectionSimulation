import java.util.ArrayList;
import java.util.Collection;


public class SimulationService {
    protected Collection<Node> nodes = new ArrayList<>();
    protected int totalMsg;

    public int getRound() {
        return round;
    }

    protected int round;

    public SimulationService() {
    }

    public void setNodes(Collection<Node> nodes) {
        this.nodes = nodes;
    }

    public void startSimulation(boolean msgLog, boolean nodeLog) throws CloneNotSupportedException {
        Logger.info("Starting HS election simulation...");
        boolean allAcknowledgeLeaders = false;
        round = 0;
        totalMsg = 0;

        while (!allAcknowledgeLeaders) {
            round++;
//            System.out.println("\n");
            Logger.info("Round " + round);

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

                totalMsg += node.lastSendMessageCount();

            }
        }

        System.out.println("\n\nElection completed in " + round + " rounds with " + totalMsg + " messages sent.");

    }

    public void printNodeStates() {
        PrintBox.printInBox("Final State of Nodes");
        for (Node node : nodes) {
            System.out.println(node);
        }
    }


    public int getTotalMessages() {
        return totalMsg;
    }

}
