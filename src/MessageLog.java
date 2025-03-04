public class MessageLog {
    private final Integer round;
    private final Integer sender;
    private final Integer receiver;
    private final Message message;

    public MessageLog(Integer round, Integer sender, Integer receiver, Message message) {
        this.round = round;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }


    public Integer getRound() {
        return round;
    }

    public Integer getSender() {
        return sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageLog{" +
                "round=" + round +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", message=" + message +
                '}';
    }
}
