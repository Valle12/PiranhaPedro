package net.messages;

public class Message {
    private MessageTypes mt;

    public Message(MessageTypes mt) {
        this.mt = mt;
    }

    public MessageTypes getMessageType() {
        return mt;
    }
}
