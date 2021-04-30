package net.messages;

public class Connect extends Message {
  private int id;

  public Connect(int id) {
    super(MessageTypes.CONNECT);
    this.id = id;
  }

  public int getID() {
    return id;
  }
}
