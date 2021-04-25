package net.client;

import net.messages.Connect;
import net.messages.Disconnect;
import net.messages.Message;
import net.messages.SystemMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProtocol extends Thread {
    private Client client;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;
    private static final int port = 12975;

    public ClientProtocol(String ipAdr, Client client) {
        this.client = client;
        try {
            this.clientSocket = new Socket(ipAdr, ClientProtocol.port);
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
            this.out.writeObject(new Connect(client.getID()));
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        running = false;
        try {
            if (!clientSocket.isClosed()) {
                out.writeObject(new Disconnect(client.getID()));
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            if (!clientSocket.isClosed()) {
                out.writeObject(new SystemMessage(message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (running) {
            try {
                Message m = (Message) in.readObject();
                switch (m.getMessageType()) {
                    case SYSTEMMESSAGE:
                        System.out.println(((SystemMessage)m).getMessage());
                        break;
                    default:
                        break;
                }
            } catch (ClassNotFoundException | IOException e) {
                break;
            }
        }
    }
}
