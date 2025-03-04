package simulation;

import static simulation.MessageType.LEADER_ANNOUNCEMENT;
import static simulation.MessageType.OUT;
import static simulation.NodeType.LEADER;
import static simulation.NodeType.SUBORDINATE;
import static simulation.Port.LEFT;
import static simulation.Port.RIGHT;

public class LCRNode extends Node {
    private Integer curMaxId;

    public LCRNode(int id) {
        super(id);
        this.curMaxId = id;
    }

    @Override
    public boolean start() {
        curMaxId = id;
        sendMessageTo(RIGHT, new Message(curMaxId, OUT));
        return true;
    }

    @Override
    public boolean processMessages() {
        boolean sent = false;
        if (buffer.get(LEFT) == null || terminated || getNodeType().equals(LEADER)) {
            return false;
        }

        Message message = buffer.get(LEFT);
        buffer.put(LEFT, null);
        Message sendRightMsg = null;

        if (message.getMsgType().equals(OUT)) {
            if (message.getMsgContent() > curMaxId) {
                curMaxId = message.getMsgContent();
                sendRightMsg = message;
            } else if (message.getMsgContent().equals(id)) {
                nodeType = LEADER;
                leaderId = this.id;
                sendRightMsg = new Message(id, LEADER_ANNOUNCEMENT);
                terminate();
            }
        } else if (message.getMsgType().equals(LEADER_ANNOUNCEMENT)) {
            nodeType = SUBORDINATE;
            leaderId = message.getMsgContent();
            sendRightMsg = message;
        }

        if (sendRightMsg != null) {
            sendMessageTo(RIGHT, sendRightMsg);
            sent = true;
        }

        return sent;
    }
}