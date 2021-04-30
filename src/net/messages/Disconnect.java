package net.messages;

public class Disconnect extends Message {
  private int id;

  public Disconnect(int id) {
    super(MessageTypes.DISCONNECT);
    this.id = id;
  }

  public int getID() {
    return id;
  }
}
