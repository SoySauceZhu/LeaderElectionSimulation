package entity.hs;

public class Message {
    private MessageType msgDirection;
    private Integer msgContent;
    private Integer hopCount;

    public Message(MessageType messageType, Integer msgContent, Integer hopCount) {
        this.msgDirection = messageType;
        this.msgContent = msgContent;
        this.hopCount = hopCount;
    }
}
