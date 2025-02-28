package entity.common;

public class Message {
    final Integer msgContent;
    final MessageType msgType;
    final Integer hopCount;

    // LCR message
    public Message(Integer msgContent, MessageType msgType) {
        this.msgContent = msgContent;
        this.msgType = msgType;
        this.hopCount = null;
    }

    // HS message
    public Message(Integer msgContent, MessageType msgType, Integer hopCount) {
        this.msgContent = msgContent;
        this.msgType = msgType;
        this.hopCount = hopCount;
    }

    public Integer getMsgContent() {
        return msgContent;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public Integer getHopCount() {
        return hopCount;
    }

    @Override
    public String toString() {
        return "{"
                + getMsgContent() + ", "
                + getMsgType() + ", "
                + getHopCount() + "}";
    }
}
