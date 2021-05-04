package net.messages;

public class DisconnectMessage extends Message {
  private int id;

  public DisconnectMessage(int id) {
    super(MessageTypes.DISCONNECT);
    this.id = id;
  }

  public int getID() {
    return id;
  }
}
