package entity.hs;

import entity.common.Status;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Node {
    private final Integer id;
    private Status status = Status.UNKNOWN;
    private Map<Port, Node> neighbors;      // Only sent message to clockwise
    private Integer leaderId;
    private boolean terminated = false;
    private final Map<Port, Queue<Message>> messageQueueMap = new HashMap<>();
    private final Map<Port, Message> buffer = new HashMap<>();
    private final Map<Port, Message> lastSentMessage = new HashMap<>();
    private Integer phase;

    public Node(int id) {
        this.id = id;
        this.phase = 0;
        messageQueueMap.put(Port.LEFT, new LinkedList<>());
        messageQueueMap.put(Port.RIGHT, new LinkedList<>());
    }

    public boolean start() {
        Message leftMsg = new Message(this.id, MessageType.OUT, 1);
        Message rightMsg = new Message(this.id, MessageType.OUT, 1);
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
        Message leftMsg = buffer.get(Port.LEFT);
        Message rightMsg = buffer.get(Port.RIGHT);

        MessageType leftMsgDirection = leftMsg.getMsgDirection();
        MessageType rightMsgDirection = rightMsg.getMsgDirection();
        Integer leftMsgId = leftMsg.getId();
        Integer rightMsgId = rightMsg.getId();
        Integer leftMsgHopCount = leftMsg.getHopCount();
        Integer rightMsgHopCount = rightMsg.getHopCount();

        if (rightMsgDirection.equals(MessageType.OUT)) {
            if (rightMsgId > id && rightMsgHopCount > 1) {
                sendLeft(new Message(rightMsgId, MessageType.OUT, rightMsgHopCount - 1));
            } else if (rightMsgId > id && rightMsgHopCount == 1) {
                sendRight(new Message(rightMsgId, MessageType.IN, 1));
            } else if (rightMsgId.equals(id)) {
                status = Status.LEADER;
                System.out.println("Node " + id + " is the leader");
            }
        }

        if (leftMsgDirection.equals(MessageType.OUT)) {
            if (leftMsgId > id && leftMsgHopCount > 1) {
                sendRight(new Message(leftMsgId, MessageType.OUT, leftMsgHopCount - 1));
            } else if (leftMsgId > id && leftMsgHopCount == 1) {
                sendLeft(new Message(leftMsgId, MessageType.IN, 1));
            } else if (leftMsgId.equals(id)) {
                status = Status.LEADER;
                System.out.println("Node " + id + " is the leader");
            }
        }

        if (rightMsgDirection.equals(MessageType.IN)
                && !rightMsgId.equals(id)) {
            sendLeft(new Message(rightMsgId, MessageType.IN, 1));
        }

        if (leftMsgDirection.equals(MessageType.IN)
                && !leftMsgId.equals(id)) {
            sendRight(new Message(leftMsgId, MessageType.IN, 1));
        }

        if (rightMsgDirection.equals(MessageType.IN)
                && leftMsgDirection.equals(MessageType.IN)
                && rightMsgId.equals(id)
                && leftMsgId.equals(id)) {
            phase++;
            sendRight(new Message(id, MessageType.OUT, (int) Math.pow(2, phase)));
            sendLeft(new Message(id, MessageType.OUT, (int) Math.pow(2, phase)));
        }


        return true;
    }

    private void receiveOutMsg(Message msg) {
        if (msg.getId() > id && msg.getHopCount() > 1) {
            sendRight(new Message(msg.getId(), MessageType.OUT, msg.getHopCount() - 1));
        } else if (msg.getId() > id && msg.getHopCount() == 1) {
            sendLeft(new Message(msg.getId(), MessageType.IN, 1));
        } else if (msg.getId().equals(id)) {
            status = Status.LEADER;
            System.out.println("Node " + id + " is the leader");
        }
    }


    public void sendMessage(Message leftMsg, Message rightMsg) {
        Node left = neighbors.get(Port.LEFT);
        Node right = neighbors.get(Port.RIGHT);
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
