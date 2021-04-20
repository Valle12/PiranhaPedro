package net.messages;

import java.io.Serializable;

public class Connect extends Message implements Serializable {
    private int id;

    public Connect(int id) {
        super(MessageTypes.CONNECT);
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
