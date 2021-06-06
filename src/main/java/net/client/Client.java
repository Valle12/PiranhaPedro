package net.client;

import game.Board;
import game.Frame;

import java.util.Arrays;

public class Client {
  private ClientProtocol connection;
  private static final String ipAdr = "192.168.178.30";
  private int id;
  private int port;
  private Frame frame;

  public Client(int id, Frame frame, int port) {
    this.id = id;
    this.frame = frame;
    this.port = port;
  }

  // Messages

  public void connect() {
    connection = new ClientProtocol(Client.ipAdr, this, port);
    connection.start();
  }

  public void connect(Board board) {
    connection = new ClientProtocol(Client.ipAdr, this, port, board);
    connection.start();
  }

  public void disconnect() {
    connection.disconnect();
  }

  public void sendMessage(String message) {
    connection.sendMessage(message);
  }

  public void updatePlayCards(int playCard, int index, boolean firstPlayer) {
    connection.updatePlayCards(playCard, index, firstPlayer);
  }

  public void updatePiranha(int index, int value, int i, int j) {
    connection.updatePiranha(index, value, i, j);
  }

  public void setAgent(int index, int value) {
    connection.setAgent(index, value);
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
