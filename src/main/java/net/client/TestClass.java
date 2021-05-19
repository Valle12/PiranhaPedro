package net.client;

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
