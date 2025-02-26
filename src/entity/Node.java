package entity;

import java.util.LinkedList;
import java.util.Queue;

public class Node {
    private Integer id;
    private Integer curMaxId;
    private Status status = Status.UNKNOWN;
    private Node next;
    private Integer leaderId;
    private boolean terminated = false;
    private Queue<Message> messageQueue = new LinkedList<>(); // Stores incoming messages
    private Message buffer = null;

    public Node(int id) {
        this.id = id;
        this.curMaxId = id; // Initially, each node sends its own ID
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void start() {
        sendMessage(new Message(MessageType.ELECTION, curMaxId));
    }

    public void readBuffer() {
        buffer = messageQueue.poll();
    }


    public void processMessages() {
        if (buffer != null && !terminated) {
            Message message = buffer;
            buffer = null;
            MessageType msgType = message.getMsgType();
            Integer msgContent = message.getMsgContent();

            if (msgType == MessageType.ELECTION) {
                if (msgContent > curMaxId) {
                    curMaxId = msgContent;
                    sendMessage(message);
                } else if (msgContent.equals(id)) { // If it receives its own ID, it wins
                    status = Status.LEADER;
                    this.leaderId = this.id;
                    System.out.println("Node " + id + " received its own ID. It is the leader.");
                    sendMessage(new Message(MessageType.LEADER_ANNOUNCEMENT, id));
                    terminate();
                }
            } else if (msgType == MessageType.LEADER_ANNOUNCEMENT) {
                status = Status.SUBORDINATE;
                this.leaderId = this.id;
//                System.out.println("Node " + id + " acknowledges Leader " + msgContent);
                sendMessage(message);
                terminate();
            }


        }
    }

    public void sendMessage(Message message) {
        next.messageQueue.add(message);
        System.out.println("Node " + id + " sent message " + message);
    }


    public void terminate() {
        terminated = true;
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

    public Node getNext() {
        return next;
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

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", curMaxId=" + curMaxId +
                ", status=" + status +
//                ", next=" + next +
                ", leaderId=" + leaderId +
                ", terminated=" + terminated +
                ", messageQueue=" + messageQueue +
                ", buffer=" + buffer +
                '}';
    }
}
