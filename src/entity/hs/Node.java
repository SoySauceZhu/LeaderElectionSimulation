package entity.hs;

import entity.common.Status;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static entity.common.MessageType.*;
import static entity.hs.Port.LEFT;
import static entity.hs.Port.RIGHT;

public class Node {
    private final Integer id;
    private Status status = Status.UNKNOWN;
    private final Map<Port, Node> neighbors = new HashMap<>();      // Only sent message to clockwise
    private Integer leaderId;
    private boolean terminated = false;
    private final Map<Port, Queue<Message>> messageQueueMap = new HashMap<>();
    private final Map<Port, Message> buffer = new HashMap<>();
    private final Map<Port, Message> lastSentMessage = new HashMap<>();
    private Integer phase;

    public Node(int id) {
        this.id = id;
        this.phase = 0;
        messageQueueMap.put(LEFT, new LinkedList<>());
        messageQueueMap.put(RIGHT, new LinkedList<>());
    }

    public boolean start() {
        Message leftMsg = new Message(this.id, OUT, 1);
        Message rightMsg = new Message(this.id, OUT, 1);
        sendLeft(leftMsg);
        sendRight(rightMsg);
        return true;
    }

    public void readBuffer() {
        Message leftMsg = messageQueueMap.get(LEFT).poll();
        Message rightMsg = messageQueueMap.get(RIGHT).poll();
        buffer.put(LEFT, leftMsg);
        buffer.put(RIGHT, rightMsg);
    }

    public boolean processMessages() {
        boolean sent = false;
        if (terminated || status.equals(Status.LEADER)) {
            return false;
        }

        Message leftMsg = buffer.get(LEFT);
        Message rightMsg = buffer.get(RIGHT);
        Message sendLeftMsg = null;
        Message sendRightMsg = null;
        Message lastSentLeftMsg = lastSentMessage.get(LEFT);
        Message lastSentRightMsg = lastSentMessage.get(RIGHT);

        if (rightMsg != null && rightMsg.getMsgType().equals(LEADER_ANNOUNCEMENT)) {
            leaderId = rightMsg.getId();
            status = Status.SUBORDINATE;
            sendLeftMsg = new Message(leaderId, LEADER_ANNOUNCEMENT, 1);
            terminate();
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(LEADER_ANNOUNCEMENT)) {
            leaderId = leftMsg.getId();
            status = Status.SUBORDINATE;
            sendRightMsg = new Message(leaderId, LEADER_ANNOUNCEMENT, 1);
            terminate();
        }

        if (rightMsg != null && rightMsg.getMsgType().equals(OUT)) {
            if (rightMsg.getId() > id && rightMsg.getHopCount() > 1) {
                sendLeftMsg = new Message(rightMsg.getId(), OUT, rightMsg.getHopCount() - 1);
            } else if (rightMsg.getId() > id && rightMsg.getHopCount() == 1) {
                sendRightMsg = new Message(rightMsg.getId(), IN, 1);
            } else if (rightMsg.getId().equals(id)) {
                leaderId = id;
                status = Status.LEADER;
                sendRightMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                sendLeftMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                terminate();
            }
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(OUT)) {
            if (leftMsg.getId() > id && leftMsg.getHopCount() > 1) {
                sendRightMsg = new Message(leftMsg.getId(), OUT, leftMsg.getHopCount() - 1);
            } else if (leftMsg.getId() > id && leftMsg.getHopCount() == 1) {
                sendLeftMsg = new Message(leftMsg.getId(), IN, 1);
            } else if (leftMsg.getId().equals(id)) {
                leaderId = id;
                status = Status.LEADER;
                sendRightMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                sendLeftMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                terminate();
            }
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(IN)
                && !leftMsg.getId().equals(id)) {
            sendRightMsg = new Message(leftMsg.getId(), IN, 1);
        }

        if (rightMsg != null && rightMsg.getMsgType().equals(IN)
                && !rightMsg.getId().equals(id)) {
            sendLeftMsg = new Message(rightMsg.getId(), IN, 1);
        }

        if (leftMsg != null && rightMsg != null
                && rightMsg.getMsgType().equals(IN)
                && leftMsg.getMsgType().equals(IN)
                && rightMsg.getId().equals(id)
                && leftMsg.getId().equals(id)) {
            phase++;
            sendRightMsg = new Message(id, OUT, (int) Math.pow(2, phase));
            sendLeftMsg = new Message(id, OUT, (int) Math.pow(2, phase));
        }

        if (sendLeftMsg != null) {
            sendLeft(sendLeftMsg);
            lastSentLeftMsg = sendLeftMsg;
            sent = true;
        }
        if (sendRightMsg != null) {
            sendRight(sendRightMsg);
            lastSentRightMsg = sendRightMsg;
            sent = true;
        }

        return sent;
    }


    public Map<Port, Queue<Message>> getMessageQueueMap() {
        return messageQueueMap;
    }

    public Integer getPhase() {
        return phase;
    }

    public void sendLeft(Message leftMsg) {
        Node left = neighbors.get(LEFT);
        // Add message to the right port of the left node
        left.messageQueueMap.get(RIGHT).add(leftMsg);
        lastSentMessage.put(LEFT, leftMsg);
        System.out.println("Node " + id + " sent message to " + left.getId() + ":  (" + leftMsg + ")");
    }

    public void sendRight(Message rightMsg) {
        Node right = neighbors.get(RIGHT);
        right.messageQueueMap.get(LEFT).add(rightMsg);
        lastSentMessage.put(RIGHT, rightMsg);
        System.out.println("Node " + id + " sent message to " + right.getId() + ": (" + rightMsg + ")");
    }

    public void terminate() {
        terminated = true;
    }

    public void setNeighbor(Port port, Node nextNode) {
        neighbors.put(port, nextNode);
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
//                ", neigbours=" + neighbors +
                ", leaderId=" + leaderId +
                ", terminated=" + terminated +
                ", messageQueue={" + messageQueueMap.get(LEFT).peek() + ", " + messageQueueMap.get(RIGHT).peek() + "}" +
                ", phase=" + phase +
                ", buffer={" + buffer.get(LEFT) + ", " + buffer.get(RIGHT) + "}";
    }


}
