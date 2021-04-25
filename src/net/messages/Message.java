package net.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageTypes mt;

    public Message(MessageTypes mt) {
        this.mt = mt;
    }

    public MessageTypes getMessageType() {
        return mt;
    }
}
