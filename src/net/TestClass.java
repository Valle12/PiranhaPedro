package net;

import net.client.Client;
import net.server.Server;

public class TestClass {
    private Server server;

    public TestClass() {
        server = new Server();
        new ServerListenThread().start();
        try {
            joinClients();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void joinClients() throws InterruptedException {
        Thread.sleep(100);
        Client client1 = new Client(0);
        client1.connect();
        Thread.sleep(100);
        Client client2 = new Client(1);
        client2.connect();
        Thread.sleep(100);
        Client client3 = new Client(2);
        client3.connect();
        Thread.sleep(100);
        client1.sendMessage("Alle Clients connected");
        Thread.sleep(100);
        client2.disconnect();
        Thread.sleep(100);
        client1.sendMessage("Clients disconnected");
    }

    class ServerListenThread extends Thread {
        public void run() {
            server.listen();
        }
    }

    public static void main(String[] args) {
        new TestClass();
    }
}
