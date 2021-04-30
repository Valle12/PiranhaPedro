package net.messages;

import game.Board;

public class UpdateBoardMessage extends Message {
  private Board board;
  private int index;

  public UpdateBoardMessage(Board board, int index) {
    super(MessageTypes.UPDATEBOARD);
    this.board = board;
    this.index = index;
  }

  public Board getBoard() {
    return board;
  }

  public int getIndex() {
    return index;
  }
}
