
public class HSNode extends Node {
    public HSNode(int id) {
        super(id);
    }

    @Override
    public boolean start() {
        Message leftMsg = new Message(id, MessageType.OUT, 1);
        Message rightMsg = new Message(id, MessageType.OUT, 1);
        sendMessageTo(Port.LEFT, leftMsg);
        sendMessageTo(Port.LEFT, rightMsg);
        return true;
    }

    @Override
    public boolean processMessages() {
        boolean sent = false;
        if (terminated || nodeType.equals(NodeType.LEADER)) {
            return false;
        }

        Message leftMsg = buffer.get(Port.LEFT);
        Message rightMsg = buffer.get(Port.LEFT);
        buffer.put(Port.LEFT, null);
        buffer.put(Port.LEFT, null);
        Message sendLeftMsg = null;
        Message sendRightMsg = null;

        if (rightMsg != null && rightMsg.getMsgType().equals(MessageType.LEADER_ANNOUNCEMENT)) {
            leaderId = rightMsg.getMsgContent();
            nodeType = NodeType.SUBORDINATE;
            sendLeftMsg = new Message(leaderId, MessageType.LEADER_ANNOUNCEMENT, 1);
            terminate();
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(MessageType.LEADER_ANNOUNCEMENT)) {
            leaderId = leftMsg.getMsgContent();
            nodeType = NodeType.SUBORDINATE;
            sendRightMsg = new Message(leaderId, MessageType.LEADER_ANNOUNCEMENT, 1);
            terminate();
        }

        if (rightMsg != null && rightMsg.getMsgType().equals(MessageType.OUT)) {
            if (rightMsg.getMsgContent() > id && rightMsg.getHopCount() > 1) {
                sendLeftMsg = new Message(rightMsg.getMsgContent(), MessageType.OUT, rightMsg.getHopCount() - 1);
            } else if (rightMsg.getMsgContent() > id && rightMsg.getHopCount() == 1) {
                sendRightMsg = new Message(rightMsg.getMsgContent(), MessageType.IN, 1);
            } else if (rightMsg.getMsgContent().equals(id)) {
                leaderId = id;
                nodeType = NodeType.LEADER;
                sendRightMsg = new Message(id, MessageType.LEADER_ANNOUNCEMENT, 1);
                sendLeftMsg = new Message(id, MessageType.LEADER_ANNOUNCEMENT, 1);
                terminate();
            }
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(MessageType.OUT)) {
            if (leftMsg.getMsgContent() > id && leftMsg.getHopCount() > 1) {
                sendRightMsg = new Message(leftMsg.getMsgContent(), MessageType.OUT, leftMsg.getHopCount() - 1);
            } else if (leftMsg.getMsgContent() > id && leftMsg.getHopCount() == 1) {
                sendLeftMsg = new Message(leftMsg.getMsgContent(), MessageType.IN, 1);
            } else if (leftMsg.getMsgContent().equals(id)) {
                leaderId = id;
                nodeType = NodeType.LEADER;
                sendRightMsg = new Message(id, MessageType.LEADER_ANNOUNCEMENT, 1);
                sendLeftMsg = new Message(id, MessageType.LEADER_ANNOUNCEMENT, 1);
                terminate();
            }
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(MessageType.IN)
                && !leftMsg.getMsgContent().equals(id)) {
            sendRightMsg = new Message(leftMsg.getMsgContent(), MessageType.IN, 1);
        }

        if (rightMsg != null && rightMsg.getMsgType().equals(MessageType.IN)
                && !rightMsg.getMsgContent().equals(id)) {
            sendLeftMsg = new Message(rightMsg.getMsgContent(), MessageType.IN, 1);
        }

        if (rightMsg != null && leftMsg != null
                && rightMsg.getMsgType().equals(MessageType.IN)
                && leftMsg.getMsgType().equals(MessageType.IN)
                && rightMsg.getMsgContent().equals(id)
                && leftMsg.getMsgContent().equals(id)) {
            phase++;
            sendLeftMsg = new Message(id, MessageType.OUT, (int) Math.pow(2, phase));
            sendRightMsg = new Message(id, MessageType.OUT, (int) Math.pow(2, phase));
        }

        if (sendLeftMsg != null) {
            sendMessageTo(Port.LEFT, sendLeftMsg);
            sent = true;
        }

        if (sendRightMsg != null) {
            sendMessageTo(Port.LEFT, sendRightMsg);
            sent = true;
        }

        return sent;
    }
}
