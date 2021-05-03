package net.messages;

import game.Board;

public class UpdatePlayCardsMessage extends Message {
  private Board board;
  private int index;

  public UpdatePlayCardsMessage(Board board, int index) {
    super(MessageTypes.UPDATEPLAYCARDS);
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
