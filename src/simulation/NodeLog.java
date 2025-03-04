package simulation;

public class NodeLog {
    private final Integer round;
    private final Node node;


    public NodeLog(Integer round, Node node) {
        this.round = round;
        this.node = node;
    }

    public Integer getRound() {
        return round;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "NodeLog{" +
                "round=" + round +
                ", node=" + node +
                '}';
    }

}
