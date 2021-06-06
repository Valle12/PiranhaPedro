package net.server;

import ai.Agent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.Board;
import game.Gameplay;
import net.messages.Message;
import net.messages.RepaintBoardMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  private int port;
  private ServerSocket serverSocket;
  private boolean running;
  private ArrayList<ServerProtocol> clients = new ArrayList<>();
  private ArrayList<Integer> clientIDs = new ArrayList<>();
  private Gameplay game;
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private Agent ai1, ai2;

  /**
   * Constructor for creating Server with special port number
   *
   * @param port Requires port Server should run on
   */
  public Server(int port) {
    this.port = port;
  }

  public synchronized void addNewClientID(int id) {
    clientIDs.add(id);
  }

  public synchronized void removeClientID(int id) {
    clientIDs.remove(id);
  }

  public synchronized void removeServerProtocol(ServerProtocol sp) {
    clients.remove(sp);
  }

  public synchronized void sendToAll(Message m) {
    int index = 0;
    boolean sendFail = false;
    for (ServerProtocol i : clients) {
      try {
        i.sendToClient(m);
      } catch (IOException e) {
        index = clients.indexOf(i);
        sendFail = true;
        continue;
      }
    }
    if (sendFail) {
      clients.remove(index);
    }
  }

  public synchronized void setPlayCard(int index, int playCard) {
    game.setPlayCard(index, playCard);
  }

  public synchronized void createGameplay(Board board) {
    game = new Gameplay(this, board);
  }

  public synchronized Board getGameBoard() {
    return game.getGameBoard();
  }

  public synchronized void repaintBoard() {
    sendToAll(new RepaintBoardMessage(gson.toJson(game.getGameBoard())));
  }

  public synchronized void setCard(int index, boolean firstPlayer) {
    game.setCard(index, firstPlayer);
  }

  public synchronized void setBoard(int i, int j) {
    game.setBoard(i, j);
  }

  public synchronized boolean setPiranhas(int index, int value) {
    return game.setPiranhas(index, value);
  }

  public synchronized void setAi1(Agent ai1) {
    this.ai1 = ai1;
  }

  public synchronized void setAi2(Agent ai2) {
    this.ai2 = ai2;
  }

  public synchronized Agent getAi1() {
    return ai1;
  }

  public synchronized Agent getAi2() {
    return ai2;
  }

  public synchronized Gameplay getGameplay() {
    return game;
  }

  public void listen() {
    running = true;
    try {
      serverSocket = new ServerSocket(port);
      while (running) {
        Socket clientSocket = serverSocket.accept();
        ServerProtocol clientThread = new ServerProtocol(clientSocket, this);
        clients.add(clientThread);
        clientThread.start();
      }
    } catch (IOException e) {
      if (serverSocket != null && serverSocket.isClosed()) {
        System.out.println("Server stopped.");
      } else {
        e.printStackTrace();
      }
    }
  }
}
