package net.messages;

import game.Board;

public class ConnectMessage extends Message {
  private int id;
  private Board board;

  public ConnectMessage(int id) {
    super(MessageTypes.CONNECT);
    this.id = id;
  }

  public int getID() {
    return id;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }
}
