package net.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Board;
import net.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProtocol extends Thread {
  private Client client;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;
  private static final int port = 12975;
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();;

  public ClientProtocol(String ipAdr, Client client) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ipAdr, ClientProtocol.port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.out.writeObject(new ConnectMessage(client.getID()));
      this.out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ClientProtocol(String ipAdr, Client client, Board board) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ipAdr, ClientProtocol.port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      ConnectMessage cm = new ConnectMessage(client.getID());
      cm.setBoard(gson.toJson(board));
      this.out.writeObject(cm);
      this.out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        out.writeObject(new DisconnectMessage(client.getID()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String message) {
    try {
      if (!clientSocket.isClosed()) {
        out.writeObject(new SystemMessage(message));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updatePlayCards(int playCard, int index, boolean firstPlayer) {
    try {
      if (!clientSocket.isClosed()) {
        out.writeObject(new UpdatePlayCardsMessage(playCard, index, firstPlayer));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updatePiranha(int index, int value, int i, int j) {
    try {
      if (!clientSocket.isClosed()) {
        out.writeObject(new UpdatePiranhaMessage(index, value, i, j));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case CONNECT:
            String board = ((ConnectMessage) m).getBoard();
            client.setGameBoard(gson.fromJson(board, Board.class));
            break;
          case SYSTEMMESSAGE:
            System.out.println(((SystemMessage) m).getMessage());
            break;
          case REPAINTBOARD:
            board = ((RepaintBoardMessage) m).getBoard();
            client.setGameBoard(gson.fromJson(board, Board.class));
            break;
          default:
            break;
        }
      } catch (ClassNotFoundException | IOException e) {
        break;
      }
    }
  }
}
