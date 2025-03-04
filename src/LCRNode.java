
public class LCRNode extends Node {
    private Integer curMaxId;
    private final Port LEFT = Port.LEFT;
    private final Port RIGHT = Port.RIGHT;
    private final MessageType LEADER_ANNOUNCEMENT = MessageType.LEADER_ANNOUNCEMENT;
    private final MessageType OUT = MessageType.OUT;
    private final NodeType LEADER = NodeType.LEADER;
    private final NodeType SUBORDINATE = NodeType.SUBORDINATE;

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