package entity.lcr;

import entity.common.Message;
import entity.common.Node;

import static entity.common.MessageType.LEADER_ANNOUNCEMENT;
import static entity.common.MessageType.OUT;
import static entity.common.NodeType.LEADER;
import static entity.common.NodeType.SUBORDINATE;
import static entity.common.Port.LEFT;
import static entity.common.Port.RIGHT;

public class LCRNode extends Node {
    private Integer curMaxId;

    public LCRNode(int id) {
        super(id);
        this.curMaxId = id;
    }

    @Override
    public boolean start() {
        sendMessageTo(RIGHT, new Message(curMaxId, OUT));
        return true;
    }

    @Override
    public boolean processMessages() {
        boolean sent = false;
        if (buffer.get(LEFT) == null || terminated || getStatus().equals(LEADER)) {
            return false;
        }

        Message message = buffer.get(LEFT);
        buffer.put(LEFT, null);
        Message sendRightMsg = null;

        if (message.getMsgType().equals(OUT)) {
            if (message.getMsgContent() > curMaxId) {
                curMaxId = message.getHopCount();
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