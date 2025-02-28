package entity.lcr;


public class Message {
    private MessageType msgType;        // message type ID
    private Integer msgContent;

    public Message(MessageType msgType, Integer msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;
    }


    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public void setMsgContent(Integer msgContent) {
        this.msgContent = msgContent;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public Integer getMsgContent() {
        return msgContent;
    }

    @Override
    public String toString() {
        return "{" + msgType + ", " + msgContent + "}";
    }
}
