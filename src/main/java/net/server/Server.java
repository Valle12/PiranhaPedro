package net.server;

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
import java.util.Arrays;

public class Server {
  private static final int port = 12975;
  private ServerSocket serverSocket;
  private boolean running;
  private ArrayList<ServerProtocol> clients = new ArrayList<>();
  private ArrayList<Integer> clientIDs = new ArrayList<>();
  private int[] playCards = new int[] {-1, -1, -1};
  private Gameplay game;

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
    System.out.println(
        "IN SERVER: "
            + Arrays.toString(game.getGameBoard().getPlayCards())
            + ", "
            + Arrays.toString(game.getGameBoard().getLowerCards())
            + ", "
            + Arrays.toString(game.getGameBoard().getUpperCards()));
    sendToAll(new RepaintBoardMessage(new Board(game.getGameBoard())));
  }

  public synchronized void setCard(int index, boolean firstPlayer) {
    game.setCard(index, firstPlayer);
  }

  public void listen() {
    running = true;
    try {
      serverSocket = new ServerSocket(Server.port);
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
