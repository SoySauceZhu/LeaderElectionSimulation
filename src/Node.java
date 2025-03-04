import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public abstract class Node implements Cloneable {
    protected final Integer id;
    protected NodeType nodeType = NodeType.UNKNOWN;
    protected Map<Port, Node> neighbors = new HashMap<>();      // Only sent message to clockwise
    protected Integer leaderId;
    protected boolean terminated = false;
    protected Map<Port, Queue<Message>> messageQueueMap = new HashMap<>();
    protected Map<Port, Message> buffer = new HashMap<>();
    protected Map<Port, Message> lastSentMessage = new HashMap<>();
    protected Integer phase;

    public Node(int id) {
        this.id = id;
        this.phase = 0;
        messageQueueMap.put(Port.LEFT, new LinkedList<>());
        messageQueueMap.put(Port.RIGHT, new LinkedList<>());
    }


    public abstract boolean start();

    public abstract boolean processMessages();


    public void setNeighbor(Port port, Node node) {
        this.neighbors.put(port, node);
    }

    protected void clearLastSent() {
        lastSentMessage.put(Port.RIGHT, null);
        lastSentMessage.put(Port.LEFT, null);
    }

    protected void sendMessageTo(Port port, Message message) {
        Logger.info("Node " + id + " sends to " + neighbors.get(port).getId() + " : {" + message + "}");
        if (port.equals(Port.LEFT)) {
            lastSentMessage.put(Port.LEFT, message);
            // Add message to the left neighbor's right message queue
            Node leftNode = neighbors.get(Port.LEFT);
            leftNode.messageQueueMap.get(Port.RIGHT).add(message);
        } else {
            lastSentMessage.put(Port.RIGHT, message);
            // Add message to the right neighbor's left message queue
            Node rightNode = neighbors.get(Port.RIGHT);
            rightNode.messageQueueMap.get(Port.LEFT).add(message);
        }
    }

    public void readBuffer() {
        clearLastSent();
        Message leftMsg = messageQueueMap.get(Port.LEFT).poll();
        Message rightMsg = messageQueueMap.get(Port.RIGHT).poll();
        buffer.put(Port.LEFT, leftMsg);
        buffer.put(Port.RIGHT, rightMsg);
    }

    protected void terminate() {
        this.terminated = true;
    }

    public Integer getId() {
        return id;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public Map<Port, Node> getNeighbors() {
        return neighbors;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public Map<Port, Queue<Message>> getMessageQueueMap() {
        return messageQueueMap;
    }

    public Map<Port, Message> getBuffer() {
        return buffer;
    }

    public Map<Port, Message> getLastSentMessage() {
        return lastSentMessage;
    }

    public Integer getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        Integer left = getNeighbors().get(Port.LEFT) != null ? getNeighbors().get(Port.LEFT).getId() : null;
        Integer right = getNeighbors().get(Port.RIGHT) != null ? getNeighbors().get(Port.RIGHT).getId() : null;
        return "Node{" +
                "id=" + id +
                ", status=" + nodeType +
                ", leaderId=" + leaderId +
                ", neighbours={" + left + ", " + right + "}" +
                ", terminated=" + terminated +
                ", messageQueue={" + messageQueueMap.get(Port.LEFT).peek() + ", " + messageQueueMap.get(Port.RIGHT).peek() + "}" +
                ", phase=" + phase +
                ", buffer={" + buffer.get(Port.LEFT) + ", " + buffer.get(Port.RIGHT) + "}";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Node cloned = (Node) super.clone();
        cloned.neighbors = new HashMap<>(this.neighbors);
        cloned.messageQueueMap = new HashMap<>();
        for (Map.Entry<Port, Queue<Message>> entry : this.messageQueueMap.entrySet()) {
            cloned.messageQueueMap.put(entry.getKey(), new LinkedList<>(entry.getValue()));
        }
        cloned.buffer = new HashMap<>(this.buffer);
        cloned.lastSentMessage = new HashMap<>(this.lastSentMessage);
        return cloned;
    }
}
