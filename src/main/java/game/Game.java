package game;

import net.client.Client;
import net.server.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Game class creates a JFrame, creates a Frame and is able to create a new game or join an already
 * existing game
 */
public class Game {
  private JFrame frame;
  private int width;
  private int height;
  public static int port = 12975;
  private Insets insets;
  private Frame fp;
  private Client client0, client1;
  private Server server;

  public Game(int width, int height) {
    this.width = width;
    this.height = height;
    this.frame = new JFrame("Piranha Pedro");
    this.frame.setSize(this.width, this.height);
    this.frame.setLocationRelativeTo(null);
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.setResizable(false);
    this.frame.setVisible(true);
    this.insets = this.frame.getInsets();
    this.frame.setVisible(false);
    this.width += this.insets.left + this.insets.right;
    this.height += this.insets.top + this.insets.bottom;
    this.frame.setSize(this.width, this.height);
    fp = new Frame(this);
    this.frame.add(fp);
    this.frame.setVisible(true);
  }

  /** Method for game creation. Is invoked, when player presses the "Create Game" Buttton */
  public void createGame() {
    server = new Server(port);
    new ServerListenThread().start();
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    client0 = new Client(0, fp, port);
    client0.connect(fp.getGameBoard());
  }

  /** Method for joining game. Is invoked, when player pressed the "Join Game" Button */
  public void joinGame() {
    client1 = new Client(1, fp, port);
    client1.connect();
  }

  public void updatePlayCards(int playCard, int index, boolean gameCreated) {
    if (gameCreated) {
      client0.updatePlayCards(playCard, index, true);
    } else {
      client1.updatePlayCards(playCard, index, false);
    }
  }

  public void updatePiranha(int index, int value, int i, int j, boolean gameCreated) {
    if (gameCreated) {
      client0.updatePiranha(index, value, i, j);
    } else {
      client1.updatePiranha(index, value, i, j);
    }
  }

  public void setAgent(int index, int value, boolean gameCreated) {
    if (gameCreated) {
      client0.setAgent(index, value);
    } else {
      client1.setAgent(index, value);
    }
  }

  public void createSpecialGame(int arg) {
    fp.createSpecialGame(arg);
  }

  public void setPort(int port) {
    this.port = port;
  }

  class ServerListenThread extends Thread {
    public void run() {
      server.listen();
    }
  }
}
