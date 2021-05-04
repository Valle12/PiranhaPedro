package net.server;

import net.messages.ConnectMessage;
import net.messages.DisconnectMessage;
import net.messages.Message;
import net.messages.UpdatePlayCardsMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

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
            ConnectMessage cm = (ConnectMessage)m;
            int id = cm.getID();
            server.addNewClientID(id);
            System.out.println("Client " + id + " connected.");
            if (id == 0) {
              server.createGameplay(cm.getBoard());
            } else {
              cm.setBoard(server.getGameBoard());
              sendToClient(cm);
            }
            break;
          case DISCONNECT:
            id = ((DisconnectMessage) m).getID();
            server.removeClientID(id);
            server.removeServerProtocol(this);
            running = false;
            disconnect();
            System.out.println("Client " + id + " disconnected.");
            break;
          case SYSTEMMESSAGE:
            server.sendToAll(m);
            break;
          case UPDATEPLAYCARDS:
            UpdatePlayCardsMessage upcm = (UpdatePlayCardsMessage) m;
            server.setCard(upcm.getPlayCard(), upcm.getFirstPlayer());
            server.setPlayCard(upcm.getIndex(), upcm.getPlayCard());
            server.repaintBoard();
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
