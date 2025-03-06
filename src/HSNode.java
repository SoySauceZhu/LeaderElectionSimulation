public class HSNode extends Node {
    private final MessageType OUT = MessageType.OUT;
    private final MessageType IN = MessageType.IN;
    private final MessageType LEADER_ANNOUNCEMENT = MessageType.LEADER_ANNOUNCEMENT;
    private final Port LEFT = Port.LEFT;
    private final Port RIGHT = Port.RIGHT;
    private final NodeType LEADER = NodeType.LEADER;
    private final NodeType SUBORDINATE = NodeType.SUBORDINATE;

    public HSNode(int id) {
        super(id);
    }

    @Override
    public boolean start() {
        Message leftMsg = new Message(id, OUT, 1);
        Message rightMsg = new Message(id, OUT, 1);
        sendMessageTo(LEFT, leftMsg);
        sendMessageTo(RIGHT, rightMsg);
        return true;
    }

    @Override
    public boolean processMessages() {
        boolean sent = false;
        if (terminated || nodeType.equals(LEADER)) {
            return false;
        }

        Message leftMsg = buffer.get(LEFT);
        Message rightMsg = buffer.get(RIGHT);
        buffer.put(LEFT, null);
        buffer.put(RIGHT, null);
        Message sendLeftMsg = null;
        Message sendRightMsg = null;

        if (rightMsg != null && rightMsg.getMsgType().equals(LEADER_ANNOUNCEMENT)) {
            leaderId = rightMsg.getMsgContent();
            nodeType = SUBORDINATE;
            sendLeftMsg = new Message(leaderId, LEADER_ANNOUNCEMENT, 1);
            terminate();
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(LEADER_ANNOUNCEMENT)) {
            leaderId = leftMsg.getMsgContent();
            nodeType = SUBORDINATE;
            sendRightMsg = new Message(leaderId, LEADER_ANNOUNCEMENT, 1);
            terminate();
        }

        if (rightMsg != null && rightMsg.getMsgType().equals(OUT)) {
            if (rightMsg.getMsgContent() > id && rightMsg.getHopCount() > 1) {
                sendLeftMsg = new Message(rightMsg.getMsgContent(), OUT, rightMsg.getHopCount() - 1);
            } else if (rightMsg.getMsgContent() > id && rightMsg.getHopCount() == 1) {
                sendRightMsg = new Message(rightMsg.getMsgContent(), IN, 1);
            } else if (rightMsg.getMsgContent().equals(id)) {
                leaderId = id;
                nodeType = LEADER;
                sendRightMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                sendLeftMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                terminate();
            }
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(OUT)) {
            if (leftMsg.getMsgContent() > id && leftMsg.getHopCount() > 1) {
                sendRightMsg = new Message(leftMsg.getMsgContent(), OUT, leftMsg.getHopCount() - 1);
            } else if (leftMsg.getMsgContent() > id && leftMsg.getHopCount() == 1) {
                sendLeftMsg = new Message(leftMsg.getMsgContent(), IN, 1);
            } else if (leftMsg.getMsgContent().equals(id)) {
                leaderId = id;
                nodeType = LEADER;
                sendRightMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                sendLeftMsg = new Message(id, LEADER_ANNOUNCEMENT, 1);
                terminate();
            }
        }

        if (leftMsg != null && leftMsg.getMsgType().equals(IN) && !leftMsg.getMsgContent().equals(id)) {
            sendRightMsg = new Message(leftMsg.getMsgContent(), IN, 1);
        }

        if (rightMsg != null && rightMsg.getMsgType().equals(IN) && !rightMsg.getMsgContent().equals(id)) {
            sendLeftMsg = new Message(rightMsg.getMsgContent(), IN, 1);
        }

        if (rightMsg != null && leftMsg != null && rightMsg.getMsgType().equals(IN) && leftMsg.getMsgType().equals(IN) && rightMsg.getMsgContent().equals(id) && leftMsg.getMsgContent().equals(id)) {
            phase++;
            sendLeftMsg = new Message(id, OUT, (int) Math.pow(2, phase));
            sendRightMsg = new Message(id, OUT, (int) Math.pow(2, phase));
        }

        if (sendLeftMsg != null) {
            sendMessageTo(LEFT, sendLeftMsg);
            sent = true;
        }

        if (sendRightMsg != null) {
            sendMessageTo(RIGHT, sendRightMsg);
            sent = true;
        }

        return sent;
    }
}