package net.server;

import net.messages.Connect;
import net.messages.Disconnect;
import net.messages.Message;
import net.messages.UpdateBoardMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerProtocol extends Thread {
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private boolean running = true;

  public ServerProtocol(Socket socket, Server server) {
    this.socket = socket;
    this.server = server;
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    running = false;
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendToClient(Message m) throws IOException {
    out.writeObject(m);
    out.flush();
  }

  public void run() {
    try {
      while (running) {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case CONNECT:
            int id = ((Connect) m).getID();
            server.addNewClientID(id);
            System.out.println("Client " + id + " connected.");
            break;
          case DISCONNECT:
            id = ((Disconnect) m).getID();
            server.removeClientID(id);
            server.removeServerProtocol(this);
            running = false;
            disconnect();
            System.out.println("Client " + id + " disconnected.");
            break;
          case SYSTEMMESSAGE:
            server.sendToAll(m);
            break;
          case UPDATEBOARD:
            UpdateBoardMessage ubm = (UpdateBoardMessage) m;
            int[] playCards = ubm.getBoard().getPlayCards();
            int index = ubm.getIndex();
            server.addPlayCard(playCards[index], index);
            if (server.hasAllPlayCards()) {
              playCards = server.getPlayCards();
              ubm.getBoard().setPlayCards(0, playCards[0]);
              ubm.getBoard().setPlayCards(1, playCards[1]);
              ubm.getBoard().setPlayCards(2, playCards[2]);
              server.sendToAll(ubm);
            }
            break;
          default:
            break;
        }
      }
    } catch (IOException e) {
      running = false;
      if (socket.isClosed()) {
        System.out.println("Socket was closed.");
      } else {
        try {
          socket.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      }
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
