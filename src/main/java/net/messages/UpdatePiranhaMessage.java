package net.messages;

public class UpdatePiranhaMessage extends Message {
  private int index, value, i, j;

  public UpdatePiranhaMessage(int index, int value, int i, int j) {
    super(MessageTypes.UPDATEPIRANHA);
    this.index = index;
    this.value = value;
    this.i = i;
    this.j = j;
  }

  public int getIndex() {
    return index;
  }

  public int getValue() {
    return value;
  }

  public int getI() {
    return i;
  }

  public int getJ() {
    return j;
  }
}
