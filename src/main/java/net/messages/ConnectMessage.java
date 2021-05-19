package net.messages;

import game.Board;

public class ConnectMessage extends Message {
  private int id;
  private String board;

  public ConnectMessage(int id) {
    super(MessageTypes.CONNECT);
    this.id = id;
  }

  public int getID() {
    return id;
  }

  public void setBoard(String board) {
    this.board = board;
  }

  public String getBoard() {
    return board;
  }
}
