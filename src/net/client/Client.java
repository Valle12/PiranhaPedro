package net.client;

import game.Board;
import game.Frame;

import java.util.Arrays;

public class Client {
  private ClientProtocol connection;
  private static final String ipAdr = "25.93.29.50";
  private int id;
  private Frame frame;

  public Client(int id, Frame frame) {
    this.id = id;
    this.frame = frame;
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

  public void updatePlayCards(Board board, int index) {
    connection.updatePlayCards(board, index);
  }

  // Getter

  public int getID() {
    return id;
  }

  public void setGameBoard(Board board) {
    frame.setGameBoard(board);
    frame.repaintBoard();
  }
}
