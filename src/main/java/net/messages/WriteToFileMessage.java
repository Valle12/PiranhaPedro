package net.messages;

public class WriteToFileMessage extends Message {
  private String fileMessage;

  public WriteToFileMessage(String fileMessage) {
    super(MessageTypes.WRITETOFILE);
  }
}
