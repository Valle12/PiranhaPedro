package net.server;

import ai.BaselineAgent;
import ai.MinMaxAgent;
import ai.RandomAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Board;
import net.messages.*;

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
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
            ConnectMessage cm = (ConnectMessage) m;
            int id = cm.getID();
            server.addNewClientID(id);
            System.out.println("Client " + id + " connected.");
            if (id == 0) {
              String board = ((ConnectMessage) m).getBoard();
              server.createGameplay(gson.fromJson(board, Board.class));
            } else {
              cm.setBoard(gson.toJson(server.getGameBoard()));
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
            break;
          case UPDATEPIRANHA:
            UpdatePiranhaMessage upm = (UpdatePiranhaMessage) m;
            if (!server.setPiranhas(upm.getIndex(), upm.getValue())) {
              break;
            } else {
              server.setBoard(upm.getI(), upm.getJ());
              server.repaintBoard();
            }
            break;
          case SETAGENT:
            SetAgentMessage sam = (SetAgentMessage) m;
            if (sam.getValue() == 0) {
              if (sam.getIndex() == 0) {
                server.setAi1(new RandomAgent(server.getGameplay(), 0));
              } else {
                server.setAi2(new RandomAgent(server.getGameplay(), 1));
              }
            } else if (sam.getValue() == 1) {
              if (sam.getIndex() == 0) {
                server.setAi1(new BaselineAgent(server.getGameplay(), 0));
              } else {
                server.setAi2(new BaselineAgent(server.getGameplay(), 1));
              }
            } else {
              if (sam.getIndex() == 0) {
                server.setAi1(new MinMaxAgent(server.getGameplay(), 0));
              } else {
                server.setAi2(new MinMaxAgent(server.getGameplay(), 1));
              }
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
