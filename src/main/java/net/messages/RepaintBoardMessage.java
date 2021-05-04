package net.messages;

import game.Board;

public class RepaintBoardMessage extends Message {
  private Board board;

  public RepaintBoardMessage(Board board) {
    super(MessageTypes.REPAINTBOARD);
    this.board = board;
  }

  public Board getBoard() {
    return new Board(board);
  }
}
