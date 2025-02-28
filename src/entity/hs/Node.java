package entity.hs;

import entity.common.MessageType;
import entity.common.Status;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static entity.common.MessageType.IN;
import static entity.common.MessageType.OUT;
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
        if ((buffer.get(LEFT) == null && buffer.get(RIGHT) == null) || terminated) {
            return false;
        }


        Message leftMsg = buffer.get(LEFT);
        Message rightMsg = buffer.get(RIGHT);
        Integer leftMsgHopCount = null;
        MessageType leftMsgType = null;
        Integer leftMsgId = null;
        MessageType rightMsgType = null;
        Integer rightMsgId = null;
        Integer rightMsgHopCount = null;

        if (leftMsg != null) {
            leftMsgType = leftMsg.getMsgType();
            leftMsgId = leftMsg.getId();
            leftMsgHopCount = leftMsg.getHopCount();
        }

        if (rightMsg != null) {
            rightMsgType = rightMsg.getMsgType();
            rightMsgId = rightMsg.getId();
            rightMsgHopCount = rightMsg.getHopCount();
        }

        if (leftMsg != null && leftMsgType.equals(MessageType.LEADER_ANNOUNCEMENT)) {
            status = Status.SUBORDINATE;
            leaderId = leftMsgId;
            terminate();
            return sendRight(leftMsg);
        }

        if (rightMsg != null && rightMsgType.equals(MessageType.LEADER_ANNOUNCEMENT)) {
            status = Status.SUBORDINATE;
            leaderId = rightMsgId;
            terminate();
            return sendLeft(rightMsg);
        }

        if (rightMsg != null && rightMsgType.equals(OUT)) {
            if (rightMsgId > id && rightMsgHopCount > 1) {
                return sendLeft(new Message(rightMsgId, OUT, rightMsgHopCount - 1));
            } else if (rightMsgId > id && rightMsgHopCount == 1) {
                return sendRight(new Message(rightMsgId, IN, 1));
            } else if (rightMsgId.equals(id)) {
                status = Status.LEADER;
                System.out.println("Node " + id + " is the leader");
                terminate();
                return sendRight(new Message(id, MessageType.LEADER_ANNOUNCEMENT, 1));
            }
        }

        if (leftMsg != null && leftMsgType.equals(OUT)) {
            if (leftMsgId > id && leftMsgHopCount > 1) {
                return sendRight(new Message(leftMsgId, OUT, leftMsgHopCount - 1));
            } else if (leftMsgId > id && leftMsgHopCount == 1) {
                sent = sendLeft(new Message(leftMsgId, IN, 1));
            } else if (leftMsgId.equals(id)) {
                status = Status.LEADER;
                System.out.println("Node " + id + " is the leader");
                terminate();
                sent = sendLeft(new Message(id, MessageType.LEADER_ANNOUNCEMENT, 1));
            }
        }

        if (rightMsg != null && rightMsgType.equals(IN) && !rightMsgId.equals(id)) {
            sent = sendLeft(new Message(rightMsgId, IN, 1));
        }

        if (leftMsg != null && leftMsgType.equals(IN) && !leftMsgId.equals(id)) {
            sent = sendRight(new Message(leftMsgId, IN, 1));
        }

        if (rightMsg != null && leftMsgType != null && rightMsgType.equals(IN) && leftMsgType.equals(IN)) {
            phase++;
            sendRight(new Message(id, OUT, (int) Math.pow(2, phase)));
            sendLeft(new Message(id, OUT, (int) Math.pow(2, phase)));
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

    public boolean sendLeft(Message leftMsg) {
        Node left = neighbors.get(LEFT);
        // Add message to the right port of the left node
        left.messageQueueMap.get(RIGHT).add(leftMsg);
        lastSentMessage.put(LEFT, leftMsg);
        System.out.println("Node " + id + " sent message to (" + left.getId() + " : " + leftMsg + ")");
        return true;
    }

    public boolean sendRight(Message rightMsg) {
        Node right = neighbors.get(RIGHT);
        right.messageQueueMap.get(LEFT).add(rightMsg);
        lastSentMessage.put(RIGHT, rightMsg);
        System.out.println("Node " + id + " sent message to (" + right.getId() + " : " + rightMsg + ")");
        return true;
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
                ", messageQueue=" + messageQueueMap +
                ", phase=" + phase +
                ", buffer=" + buffer + '}';
    }


}
