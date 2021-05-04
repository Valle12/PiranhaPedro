package net.messages;

import game.Board;

public class UpdatePlayCardsMessage extends Message {
  private int playCard, index;
  private boolean firstPlayer;

  public UpdatePlayCardsMessage(int playCard, int index, boolean firstPlayer) {
    super(MessageTypes.UPDATEPLAYCARDS);
    this.playCard = playCard;
    this.index = index;
    this.firstPlayer = firstPlayer;
  }

  public int getPlayCard() {
    return playCard;
  }

  public int getIndex() {
    return index;
  }

  public boolean getFirstPlayer() {
    return firstPlayer;
  }
}
