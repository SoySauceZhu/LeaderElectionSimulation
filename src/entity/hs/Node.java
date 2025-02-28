package entity.hs;

import entity.common.Status;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Node {
    private Integer id;
    private Integer curMaxId;
    private Status status = Status.UNKNOWN;
    private Map<String, Node> neighbors;      // Only sent message to clockwise
    private Integer leaderId;
    private boolean terminated = false;
    private Queue<Message> messageQueue = new LinkedList<>(); // Stores incoming messages
    private Message buffer = null;
    private Map<String, Message> lastSentMessage = new HashMap<>();
    private Integer phase;

    public Node(int id) {
        this.id = id;
        this.curMaxId = id; // Initially, each node sends its own ID
        this.phase = 0;
    }

    public boolean start() {
        return true;
    }

    public void readBuffer() {

    }

    public boolean processMessages() {
        return true;
    }

    public void sendMessage(Message leftMsg, Message rightMsg) {
        Node left = neighbors.get("left");
        Node right = neighbors.get("right");
        left.messageQueue.add(leftMsg);
        right.messageQueue.add(rightMsg);
        lastSentMessage.put("left", leftMsg);
        lastSentMessage.put("right", rightMsg);
        System.out.println("Node " + id + " sent message to ("
                + left.getId() + " : " + leftMsg + ", "
                + left.getId() + " : " + rightMsg + ")");
    }

    public void terminate() {
        terminated = true;
    }

    public void setNeighbors(Node left, Node right) {
        neighbors.put("left", left);
        neighbors.put("right", right);
    }


    public void setNeighbors(Map<String, Node> neighbors) {
        this.neighbors = neighbors;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCurMaxId() {
        return curMaxId;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, Node> getNeighbors() {
        return neighbors;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }

    public Message getBuffer() {
        return buffer;
    }

    public Map<String, Message> getLastSentMessage() {
        return lastSentMessage;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", curMaxId=" + curMaxId +
                ", status=" + status +
                ", leaderId=" + leaderId +
                ", terminated=" + terminated +
                ", messageQueue=" + messageQueue +
                ", buffer=" + buffer +
                '}';
    }
}
