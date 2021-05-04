package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JPanel;

public class Frame extends JPanel implements MouseListener {
  private static final long serialVersionUID = 6434731205261840192L;
  private final int CARD_AMOUNT = 12;
  private Insets insets;
  private int[][] board; // 0 = empty, 1 = land, 2 = piranha, 3 = pedro
  private int[]
      playCards; // First Entry for first player, second entry for second player, third entry for
  // player with sombrero card
  private int[] stones;
  private int[] piranhas;
  private int[] wins;
  private boolean printPossible = false;
  private boolean chooseNetwork;
  private boolean choosePlayer;
  private boolean chooseAI;
  private boolean singleplayer;
  private boolean activeAI;
  private boolean gameCreated;
  private boolean firstPlayer, secondPlayer;
  private boolean currentPlayer, choosePiranha, currentCard; // false = first player's
  // turn, true = second
  // player's turn
  private boolean[] lowerCards, upperCards; // false = card not yed played, true = card played
  private String[] directions;
  private Game game;
  private Board gameBoard;

  public Frame(Game game) {
    this.game = game;
    this.insets = this.getInsets();
    board = new int[11][15];
    predefineBoard();
    lowerCards = new boolean[CARD_AMOUNT];
    upperCards = new boolean[CARD_AMOUNT];
    directions = new String[] {"Green", "Cyan", "Magenta", "Orange"};
    addMouseListener(this);
    playCards = new int[] {-1, -1, -1};
    stones = new int[] {4, 4};
    piranhas = new int[2];
    wins = new int[2];
    currentPlayer = (Math.random() > 0.5) ? true : false;
    currentCard = currentPlayer;
    chooseNetwork = true;
    gameBoard =
        new Board(
            board,
            playCards,
            stones,
            piranhas,
            wins,
            currentPlayer,
            currentCard,
            choosePiranha,
            lowerCards,
            upperCards);

    // Last command
    printPossible = true;
    repaint();
  }

  private void predefineBoard() {
    // Piranha
    board[0][0] = 2;
    board[0][13] = 2;
    board[2][11] = 2;
    board[7][12] = 2;
    board[8][2] = 2;
    board[8][13] = 2;
    board[10][8] = 2;

    // Land
    board[2][4] = 1;
    board[3][4] = 1;
    board[3][5] = 1;
    board[4][2] = 1;
    board[4][3] = 1;
    board[4][4] = 1;
    board[4][5] = 1;
    board[5][5] = 1;

    // Pedro
    board[4][4] = 3;
  }

  public void repaintBoard() {
    repaint();
  }

  public Board getGameBoard() {
    return gameBoard;
  }

  public void setGameBoard(Board board) {
    this.gameBoard = new Board(board);
    System.out.println("IN FRAME: " + Arrays.toString(gameBoard.getPlayCards()) + ", " + Arrays.toString(gameBoard.getLowerCards()) + ", " + Arrays.toString(gameBoard.getUpperCards()));
  }

  @Override
  public void paintComponent(Graphics gOld) {
    if (printPossible) {
      super.paintComponent(gOld);
      Graphics2D g = (Graphics2D) gOld;
      g.setColor(getBackground());
      g.fillRect(insets.left, insets.top, 1000, 800);
      paintBoard(g);
      paintLowerCards(g);
      paintUpperCards(g);
      paintPlayCards(g);
      paintPedro(g);
      paintStonesAndPiranhasAndWins(g);
      paintChoosePiranhas(g);
      paintChooseNetwork(g);
      paintNetwork(g);
      paintChooseAI(g);
    }
  }

  private void paintBoard(Graphics2D g) {
    int[][] board2 = gameBoard.getBoard();
    int boardXOffset = 200 + insets.left;
    int boardYOffset = 180 + insets.top;
    for (int i = 0; i < board2.length; i++) {
      for (int j = 0; j < board2[i].length; j++) {
        switch (board2[i][j]) {
          case 0:
            g.setColor(Color.BLACK);
            g.drawRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
            break;
          case 1:
            g.setColor(Color.YELLOW);
            g.fillRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
            break;
          case 2:
            g.setColor(Color.RED);
            g.fillRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
            break;
          case 3:
            g.setColor(Color.BLUE);
            g.fillRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
            break;
        }
      }
    }
    g.setColor(Color.GREEN);
    g.fillRect(boardXOffset, boardYOffset - 20, 15 * 40, 20);
    g.setColor(Color.CYAN);
    g.fillRect(boardXOffset + 15 * 40, boardYOffset, 20, 11 * 40);
    g.setColor(Color.MAGENTA);
    g.fillRect(boardXOffset, boardYOffset + 11 * 40, 15 * 40, 20);
    g.setColor(Color.ORANGE);
    g.fillRect(boardXOffset - 20, boardYOffset, 20, 11 * 40);
  }

  private void paintLowerCards(Graphics2D g) {
    int boardXOffset = 2 + insets.left;
    int boardYOffset = 670 + insets.top;
    for (int i = 0; i < CARD_AMOUNT; i++) {
      if (!gameBoard.getLowerCards()[i]) {
        switch (i / 3) {
          case 0:
            g.setColor(Color.GREEN);
            break;
          case 1:
            g.setColor(Color.CYAN);
            break;
          case 2:
            g.setColor(Color.MAGENTA);
            break;
          case 3:
            g.setColor(Color.ORANGE);
            break;
        }
        g.fillRect(boardXOffset + i * (80 + 3), boardYOffset, 80, 80);
        g.setColor(Color.BLACK);
        g.drawString(
            ((i % 3) + 1) + " " + directions[i / 3],
            boardXOffset + i * (80 + 3) + 15,
            boardYOffset + 43);
      }
    }
    Font oldFont = g.getFont();
    Font font = new Font("Arial", Font.PLAIN, 20);
    g.setFont(font);
    g.setColor(Color.BLACK);
    g.drawString("1. Player", (int) (getSize().getWidth() / 2) - 35, boardYOffset + 80 + 30);
    g.setFont(oldFont);
  }

  private void paintUpperCards(Graphics2D g) {
    int boardXOffset = 2 + insets.left;
    int boardYOffset = 50 + insets.top;
    for (int i = 0; i < CARD_AMOUNT; i++) {
      if (!gameBoard.getUpperCards()[i]) {
        switch (i / 3) {
          case 0:
            g.setColor(Color.GREEN);
            break;
          case 1:
            g.setColor(Color.CYAN);
            break;
          case 2:
            g.setColor(Color.MAGENTA);
            break;
          case 3:
            g.setColor(Color.ORANGE);
            break;
        }
        g.fillRect(boardXOffset + i * (80 + 3), boardYOffset, 80, 80);
        g.setColor(Color.BLACK);
        g.drawString(
            ((i % 3) + 1) + " " + directions[i / 3],
            boardXOffset + i * (80 + 3) + 15,
            boardYOffset + 43);
      }
    }
    Font oldFont = g.getFont();
    Font font = new Font("Arial", Font.PLAIN, 20);
    g.setFont(font);
    g.setColor(Color.BLACK);
    g.drawString("2. Player", (int) (getSize().getWidth() / 2) - 35, boardYOffset - 18);
    g.setFont(oldFont);
  }

  private void paintPlayCards(Graphics2D g) {
    int[] playCards2 = gameBoard.getPlayCards();
    int boardXOffset = 30 + insets.left;
    int boardYMiddle = (getHeight() / 2) + insets.top;
    Font oldFont = g.getFont();
    Font font = new Font("Arial", Font.PLAIN, 15);
    g.setFont(font);
    for (int i = 0; i < playCards2.length; i++) {
      switch (playCards2[i] / 3) {
        case 0:
          g.setColor(Color.GREEN);
          break;
        case 1:
          g.setColor(Color.CYAN);
          break;
        case 2:
          g.setColor(Color.MAGENTA);
          break;
        case 3:
          g.setColor(Color.ORANGE);
          break;
      }
      if (i == 0 && (playCards2[0] != -1)) {
        g.fillRect(boardXOffset, boardYMiddle + 5, 120, 120);
        g.setColor(Color.BLACK);
        g.drawString(
            ((playCards2[0] % 3) + 1) + " " + directions[playCards2[0] / 3],
            boardXOffset + 30,
            boardYMiddle + 70);
      } else if (i == 1 && (playCards2[1] != -1)) {
        g.fillRect(boardXOffset, boardYMiddle - 125, 120, 120);
        g.setColor(Color.BLACK);
        g.drawString(
            ((playCards2[1] % 3) + 1) + " " + directions[playCards2[1] / 3],
            boardXOffset + 30,
            boardYMiddle - 60);
      } else if (i == 2
          && (gameBoard.getCurrentPlayer() ? (playCards2[1] == -1) : (playCards2[0] == -1))
          && (playCards2[2] != -1)) {
        g.fillRect(
            boardXOffset, boardYMiddle + (gameBoard.getCurrentPlayer() ? -125 : 5), 120, 120);
        g.setColor(Color.BLACK);
        g.drawString(
            ((playCards2[2] % 3) + 1) + " " + directions[playCards2[2] / 3],
            boardXOffset + 30,
            boardYMiddle + (gameBoard.getCurrentPlayer() ? -60 : 70));
      }
    }
    g.setFont(oldFont);
  }

  private void paintPedro(Graphics2D g) {
    int boardXOffset = 850 + insets.left;
    int boardYMiddle = (getHeight() / 2) + insets.top;
    g.setColor(Color.BLUE);
    if (gameBoard.getCurrentPlayer()) {
      g.fillRect(boardXOffset, boardYMiddle - 125, 120, 120);
    } else {
      g.fillRect(boardXOffset, boardYMiddle + 5, 120, 120);
    }
  }

  private void paintStonesAndPiranhasAndWins(Graphics2D g) {
    int boardXOffset = 30 + insets.left;
    int boardYMiddle = (getHeight() / 2) + insets.top;
    g.setColor(Color.BLACK);
    g.drawString("Number of Stones: " + gameBoard.getStones()[0], boardXOffset, boardYMiddle + 145);
    g.drawString("Number of Stones: " + gameBoard.getStones()[1], boardXOffset, boardYMiddle - 170);
    g.drawString(
        "Number of Piranhas: " + gameBoard.getPiranhas()[0], boardXOffset, boardYMiddle + 160);
    g.drawString(
        "Number of Piranhas: " + gameBoard.getPiranhas()[1], boardXOffset, boardYMiddle - 155);
    g.drawString("Wins Player 1: " + gameBoard.getWins()[0], boardXOffset, boardYMiddle + 175);
    g.drawString("Wins Player 2: " + gameBoard.getWins()[1], boardXOffset, boardYMiddle - 140);
  }

  private void paintChoosePiranhas(Graphics2D g) {
    if (gameBoard.getChoosePiranha()) {
      Font oldFont = g.getFont();
      Font font = new Font("Arial", Font.PLAIN, 20);
      g.setFont(font);
      g.setColor(Color.RED);
      g.drawString(
          "Choose Piranha!", 2 + insets.left, gameBoard.getCurrentCard() ? 32 + insets.top : 780);
      g.setFont(oldFont);
    }
  }

  private void paintChoosing(Graphics2D g, String first, String second) {
    Font oldFont = g.getFont();
    Font font = new Font("Arial", Font.PLAIN, 20);
    int boardXOffset = 850 + insets.left;
    g.setColor(Color.RED);
    g.fillRect(boardXOffset - 100, getHeight() - 45, 200, 40);
    g.fillRect(boardXOffset - 100, 5, 200, 40);
    g.setColor(Color.BLACK);
    g.setFont(font);
    g.drawString(first, boardXOffset - 60, getHeight() - 18);
    g.drawString(second, boardXOffset - 43, 33);
    g.setFont(oldFont);
  }

  private void paintChooseNetwork(Graphics2D g) {
    if (chooseNetwork) {
      paintChoosing(g, "Singleplayer", "Multiplayer");
    }
  }

  private void paintNetwork(Graphics2D g) {
    if (choosePlayer) {
      paintChoosing(g, "Create Game", "Join Game");
    }
  }

  private void paintChooseAI(Graphics2D g) {
    if (chooseAI) {
      paintChoosing(g, "AI", "Player");
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int[] playCards2 = gameBoard.getPlayCards();
    int x = e.getX();
    int y = e.getY();
    int cardXOffset = 2 + insets.left;
    int cardYOffsetTop = 50 + insets.top;
    int cardYOffsetBottom = 670 + insets.top;
    int boardXOffset = 200 + insets.left;
    int boardYOffset = 180 + insets.top;
    for (int i = 0; i < CARD_AMOUNT; i++) {
      if (x >= (cardXOffset + i * (80 + 3))
          && x <= (cardXOffset + i * (80 + 3) + 80)
          && y >= cardYOffsetTop
          && y <= (cardYOffsetTop + 80)) {
        if (!gameBoard.getUpperCards()[i]
            && secondPlayer
            && ((playCards2[1] == -1) || (gameBoard.getCurrentPlayer() && (playCards2[2] == -1)))) {
          int index = (playCards2[1] == -1) ? 1 : 2;
          gameBoard.setUpperCards(i, !gameBoard.getUpperCards()[i]);
          gameBoard.setPlayCard(index, i);
          game.updatePlayCards(i, index, gameCreated);
        }
      } else if (x >= (cardXOffset + i * (80 + 3))
          && x <= (cardXOffset + i * (80 + 3) + 80)
          && y >= cardYOffsetBottom
          && y <= (cardYOffsetBottom + 80)) {
        if (!gameBoard.getLowerCards()[i]
            && firstPlayer
            && ((playCards2[0] == -1)
                || (!gameBoard.getCurrentPlayer() && (playCards2[2] == -1)))) {
          int index = (playCards2[0] == -1) ? 0 : 2;
          gameBoard.setLowerCards(i, !gameBoard.getLowerCards()[i]);
          gameBoard.setPlayCard(index, i);
          game.updatePlayCards(i, index, gameCreated);
        }
      }
    }
    if (gameBoard.getChoosePiranha()) {
      int[][] board2 = gameBoard.getBoard();
      for (int i = 0; i < board2.length; i++) {
        for (int j = 0; j < board2[i].length; j++) {
          if (board2[i][j] == 2
              && x >= (boardXOffset + j * 40)
              && x < (boardXOffset + (j + 1) * 40)
              && y >= (boardYOffset + i * 40)
              && y < (boardYOffset + (i + 1) * 40)) {
            gameBoard.setBoard(i, j, 0);
            gameBoard.setPiranhas(
                gameBoard.getCurrentCard() ? 1 : 0,
                gameBoard.getPiranhas()[gameBoard.getCurrentCard() ? 1 : 0] + 1);
            if (gameBoard.getPiranhas()[0] == 3) {
              // TODO gameOver(true);
            } else if (gameBoard.getPiranhas()[1] == 3) {
              // TODO gameOver(false);
            } else {

            }
            gameBoard.setChoosePiranha(false);
            // TODO changeCurrentPlayer();
            repaint();
          } else {
            continue;
          }
        }
      }
    }
    // Choose if you want to play in Singleplayer or Multiplayer
    if (chooseNetwork) {
      // Singleplayer
      if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= (getHeight() - 45))
          && (y < (getHeight() - 5)))) {
        chooseNetwork = false;
        firstPlayer = true;
        secondPlayer = false;
        choosePlayer = false;
        chooseAI = true;
        singleplayer = true;
        // Multiplayer
      } else if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= 5)
          && (y < 45))) {
        chooseNetwork = false;
        firstPlayer = false;
        secondPlayer = false;
        choosePlayer = true;
        chooseAI = false;
        singleplayer = false;
      }
      repaint();
      return;
    }
    // Choose if you want to create a game as Player 1 or join a game as Player 2
    if (choosePlayer) {
      // Create Game
      if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= (getHeight() - 45))
          && (y < (getHeight() - 5)))) {
        choosePlayer = false;
        firstPlayer = true;
        secondPlayer = false;
        game.createGame();
        chooseAI = true;
        gameCreated = true;
        // Join Game
      } else if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= 5)
          && (y < 45))) {
        choosePlayer = false;
        firstPlayer = false;
        secondPlayer = true;
        game.joinGame();
        chooseAI = true;
        gameCreated = false;
      }
      repaint();
      return;
    }
    // In Singleplayer: Second Player is always AI, Choose if Player 1 should be AI as well
    // In Multiplayer: Choose if it should be AI or Player on Player which is chosen before
    if (chooseAI) {
      // AI
      if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= (getHeight() - 45))
          && (y < (getHeight() - 5)))) {
        chooseAI = false;
        activeAI = true;
        firstPlayer = false;
        secondPlayer = false;
        // Player
      } else if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= 5)
          && (y < 45))) {
        chooseAI = false;
        activeAI = false;
        firstPlayer = singleplayer ? true : gameCreated;
        secondPlayer = singleplayer ? false : !gameCreated;
      }
      repaint();
    }
    // TODO REMOVE
    firstPlayer = true;
    secondPlayer = true;
    repaint();
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {}

  @Override
  public void mouseExited(MouseEvent arg0) {}

  @Override
  public void mousePressed(MouseEvent arg0) {}

  @Override
  public void mouseReleased(MouseEvent arg0) {}
}
