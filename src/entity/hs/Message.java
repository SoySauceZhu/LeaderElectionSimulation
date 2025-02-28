package entity.hs;

public class Message {
    private final Integer id;
    private final MessageType msgType;
    private final Integer hopCount;

    public Message(Integer id, MessageType messageType, Integer hopCount) {
        this.msgType = messageType;
        this.id = id;
        this.hopCount = hopCount;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHopCount() {
        return hopCount;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", msgDirection=" + msgType +
                ", hopCount=" + hopCount +
                '}';
    }
}
