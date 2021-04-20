package net.client;

public class Client {
    private ClientProtocol connection;
    private final String ipAdr = "25.93.29.50";
    private int id;

    public void connect() {
        connection = new ClientProtocol(ipAdr, this);
        connection.start();
    }

    public int getID() {
        return id;
    }
}
