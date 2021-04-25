package net.messages;

public class SystemMessage extends Message {
    private String message;

    public SystemMessage(String message) {
        super(MessageTypes.SYSTEMMESSAGE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
