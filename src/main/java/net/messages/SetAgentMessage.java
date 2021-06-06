package net.messages;

public class SetAgentMessage extends Message {
  private int index;
  private int value;

  public SetAgentMessage(int index, int value) {
    super(MessageTypes.SETAGENT);
    this.index = index;
    this.value = value;
  }

  public int getIndex() {
    return index;
  }

  public int getValue() {
    return value;
  }
}
