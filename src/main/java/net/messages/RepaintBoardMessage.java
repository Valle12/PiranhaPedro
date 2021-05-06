package net.messages;

public class RepaintBoardMessage extends Message {
  private String board;

  public RepaintBoardMessage(String board) {
    super(MessageTypes.REPAINTBOARD);
    this.board = board;
  }

  public String getBoard() {
    return board;
  }
}
