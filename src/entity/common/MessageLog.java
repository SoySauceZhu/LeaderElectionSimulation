package entity.common;

public class MessageLog {
    private final int round;
    private final int sender;
    private final int receiver;
    private final MessageType messageType;
    private final int messageContent;

    public MessageLog(int round, int sender, int receiver, MessageType messageType, int messageContent) {
        this.round = round;
        this.sender = sender;
        this.receiver = receiver;
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public int getRound() {
        return round;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public int getMessageContent() {
        return messageContent;
    }
}
