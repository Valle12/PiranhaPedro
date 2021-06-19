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
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private int counter = 0;
  private int tmpIndex;
  private int tmpPlayCard;

  public ClientProtocol(String ipAdr, Client client, int port) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ipAdr, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.out.writeObject(new ConnectMessage(client.getID()));
      this.out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ClientProtocol(String ipAdr, Client client, int port, Board board) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ipAdr, port);
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

  public void setAgent(int index, int value) {
    try {
      if (!clientSocket.isClosed()) {
        out.writeObject(new SetAgentMessage(index, value));
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
          case UPDATEPLAYCARDS:
            UpdatePlayCardsMessage upcm = (UpdatePlayCardsMessage) m;
            if (!client.getFrame().getGameBoard().getCurrentPlayer()
                && upcm.getFirstPlayer()
                && (counter == 0)) {
              tmpIndex = upcm.getIndex();
              tmpPlayCard = upcm.getPlayCard();
              counter++;
            } else if (!client.getFrame().getGameBoard().getCurrentPlayer()
                && !upcm.getFirstPlayer()) {
              client
                  .getFrame()
                  .getFileGregor()
                  .writePlayCards(upcm.getPlayCard() / 3, (upcm.getPlayCard() % 3) + 1, -1, -1);
            } else if (client.getFrame().getGameBoard().getCurrentPlayer()
                && upcm.getFirstPlayer()) {
              client
                  .getFrame()
                  .getFileValentin()
                  .writePlayCards(upcm.getPlayCard() / 3, (upcm.getPlayCard() % 3) + 1, -1, -1);
            } else {
              if (counter != 1) {
                tmpIndex = upcm.getIndex();
                tmpPlayCard = upcm.getPlayCard();
              }
              counter++;
            }
            if (counter == 2) {
              if (client.getFrame().getGameBoard().getCurrentPlayer()) {
                client
                    .getFrame()
                    .getFileGregor()
                    .writePlayCards(
                        tmpPlayCard / 3, (tmpPlayCard % 3) + 1,
                        upcm.getPlayCard() / 3, (upcm.getPlayCard() % 3) + 1);
              } else {
                client
                    .getFrame()
                    .getFileValentin()
                    .writePlayCards(
                        tmpPlayCard / 3,
                        (tmpPlayCard % 3) + 1,
                        upcm.getPlayCard() / 3,
                        (upcm.getPlayCard() % 3) + 1);
              }
              counter = 0;
            }
            break;
          case UPDATEPIRANHA:
            UpdatePiranhaMessage upm = (UpdatePiranhaMessage) m;
            if (upm.getIndex() == 0) {
              client.getFrame().getFileValentin().writePiranha(upm.getI(), upm.getJ());
            } else {
              client.getFrame().getFileGregor().writePiranha(upm.getI(), upm.getJ());
            }
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
