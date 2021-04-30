package net.client;

import game.Board;

public class Client {
  private ClientProtocol connection;
  private static final String ipAdr = "25.93.29.50";
  private int id;

  public Client(int id) {
    this.id = id;
  }

  // Messages

  public void connect() {
    connection = new ClientProtocol(Client.ipAdr, this);
    connection.start();
  }

  public void disconnect() {
    connection.disconnect();
  }

  public void sendMessage(String message) {
    connection.sendMessage(message);
  }

  public void updateBoard(Board board, int index) {
    connection.updateBoard(board, index);
  }

  // Getter

  public int getID() {
    return id;
  }
}
