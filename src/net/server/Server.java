package net.server;

import net.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int port = 12975;
    private ServerSocket serverSocket;
    private static final String ipAdr = "25.93.29.50";
    private boolean running;
    private ArrayList<ServerProtocol> clients = new ArrayList<>();
    private ArrayList<Integer> clientIDs = new ArrayList<>();

    public synchronized void addNewClientID(int id) {
        clientIDs.add(id);
    }

    public synchronized void removeClientID(int id) {
        clientIDs.remove(id);
    }

    public synchronized void removeServerProtocol(ServerProtocol sp) {
        clients.remove(sp);
    }

    public synchronized void sendToAll(Message m) {
        int index = 0;
        boolean sendFail = false;
        for (ServerProtocol i : clients) {
            try {
                i.sendToClient(m);
            } catch (IOException e) {
                index = clients.indexOf(i);
                sendFail = true;
                continue;
            }
        }
        if (sendFail) {
            clients.remove(index);
        }
    }

    public void listen() {
        running = true;
        try {
            serverSocket = new ServerSocket(Server.port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                ServerProtocol clientThread = new ServerProtocol(clientSocket, this);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            if (serverSocket != null && serverSocket.isClosed()) {
                System.out.println("Server stopped.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
