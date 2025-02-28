package entity.hs;

public class Message {
    private Integer id;
    private MessageType msgDirection;
    private Integer hopCount;

    public Message(Integer id, MessageType messageType, Integer hopCount) {
        this.msgDirection = messageType;
        this.id = id;
        this.hopCount = hopCount;
    }

    public MessageType getMsgDirection() {
        return msgDirection;
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
                ", msgDirection=" + msgDirection +
                ", hopCount=" + hopCount +
                '}';
    }
}
