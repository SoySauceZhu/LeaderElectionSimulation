package entity.hs;

import entity.common.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Node {
    private Integer id;
    private Status status = Status.UNKNOWN;
    private Map<Port, Node> neighbors;      // Only sent message to clockwise
    private Integer leaderId;
    private boolean terminated = false;
    private Map<Port, Queue<Message>> messageQueueMap = new HashMap<>();
    private Map<Port, Message> buffer = new HashMap<>();
    private Map<Port, Message> lastSentMessage = new HashMap<>();
    private Integer phase;

    public Node(int id) {
        this.id = id;
        this.phase = 1;
    }

    public boolean start() {
        Message leftMsg = new Message(this.id, MessageType.OUT, phase);
        Message rightMsg = new Message(this.id, MessageType.OUT, phase);
        sendMessage(leftMsg, rightMsg);
        return true;
    }

    public void readBuffer() {
        Message leftMsg = messageQueueMap.get(Port.LEFT).poll();
        Message rightMsg = messageQueueMap.get(Port.RIGHT).poll();
        buffer.put(Port.LEFT, leftMsg);
        buffer.put(Port.RIGHT, rightMsg);
    }

    public boolean processMessages() {

        return true;
    }


    public void sendMessage(Message leftMsg, Message rightMsg) {
        Node left = neighbors.get("left");
        Node right = neighbors.get("right");
        left.messageQueueMap.get(Port.RIGHT).add(rightMsg);
        right.messageQueueMap.get(Port.LEFT).add(leftMsg);
        lastSentMessage.put(Port.LEFT, leftMsg);
        lastSentMessage.put(Port.RIGHT, rightMsg);
        System.out.println("Node " + id + " sent message to ("
                + left.getId() + " : " + leftMsg + ", "
                + left.getId() + " : " + rightMsg + ")");
    }

    public void sendLeft(Message leftMsg) {
        Node left = neighbors.get(Port.LEFT);
        // Add message to the right port of the left node
        left.messageQueueMap.get(Port.RIGHT).add(leftMsg);
        lastSentMessage.put(Port.LEFT, leftMsg);
        System.out.println("Node " + id + " sent message to ("
                + left.getId() + " : " + leftMsg + ")");
    }

    public void sendRight(Message rightMsg) {
        Node right = neighbors.get(Port.RIGHT);
        right.messageQueueMap.get(Port.LEFT).add(rightMsg);
        lastSentMessage.put(Port.RIGHT, rightMsg);
        System.out.println("Node " + id + " sent message to ("
                + right.getId() + " : " + rightMsg + ")");
    }

    public void terminate() {
        terminated = true;
    }

    public void setNeighbors(Node left, Node right) {
        neighbors.put(Port.LEFT, left);
        neighbors.put(Port.RIGHT, right);
    }

    public void setNeighbors(Map<Port, Node> neighbors) {
        this.neighbors = neighbors;
    }

    public Integer getId() {
        return id;
    }


    public Status getStatus() {
        return status;
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

    public Map<Port, Queue<Message>> getMessageQueue() {
        return messageQueueMap;
    }

    public Map<Port, Message> getBuffer() {
        return buffer;
    }

    public Map<Port, Message> getLastSentMessage() {
        return lastSentMessage;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", status=" + status +
                ", leaderId=" + leaderId +
                ", terminated=" + terminated +
                ", messageQueue=" + messageQueueMap +
                ", buffer=" + buffer +
                '}';
    }
}
