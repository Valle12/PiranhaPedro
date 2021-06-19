package game;

import ft.TxtHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Frame extends JPanel implements MouseListener {
  private static final long serialVersionUID = 6434731205261840192L;
  private final int CARD_AMOUNT = 12;
  private Insets insets;
  private int port;
  private int[] playCards; // 0: first player, 1: second player, 2: player with sombrero
  private int[] stones; // 0: lower player, 1: upper player
  private int[] piranhas; // 0: lower player, 1: upper player
  private int[] wins; // 0: lower player, 1: upper player
  private int[][] board; // 0 = empty, 1 = land, 2 = piranha, 3 = pedro
  private boolean printPossible; // Will be set true after constructor
  private boolean chooseFileBased = true; // Set to false, when clicked on FileBased or System Based
  private boolean chooseNetwork; // Set to true, if System Based
  private boolean choosePlayer;
  private boolean chooseAI;
  private boolean activeAI;
  private boolean singleplayer; // true if "Singleplayer" was selected
  private boolean gameCreated; // true if created game
  private boolean firstPlayer; // true if lower player
  private boolean secondPlayer; // true if upper player
  private boolean currentPlayer; // true: upper player, false: lower player
  private boolean currentCard; // true: upper player, false: lower player
  public static boolean systemBased;
  private boolean[] lowerCards; // false = card not yed played, true = card played
  private boolean[] upperCards; // false = card not yed played, true = card played
  private String[] directions; // From top clockwise to left direction
  private Game game; // Instance to interact with client side game
  private Board gameBoard; // Instance of current Board
  public static TxtHandler fileGregor; // Important path for FileBased
  public static TxtHandler fileValentin; // Important path for FileBased
  private final String sep = System.getProperty("file.separator");

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
    currentPlayer = Math.random() > 0.5;
    currentCard = currentPlayer;
    gameBoard =
        new Board(
            board,
            playCards,
            stones,
            piranhas,
            wins,
            currentPlayer,
            currentCard,
            false,
            lowerCards,
            upperCards);
    fileGregor =
        new TxtHandler(
            System.getProperty("user.dir") + sep + "test" + sep + "gregor.txt", this, "GREGOR");
    fileGregor.start();
    fileValentin =
        new TxtHandler(
            System.getProperty("user.dir") + sep + "test" + sep + "valentin.txt", this, "VALENTIN");
    fileValentin.start();
    fileValentin.writeConfig(currentPlayer);

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
    board[5][4] = 1;

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
  }

  public void createSingleplayerGame() {
    game.createGame();
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    game.joinGame();
  }

  public void createSpecialGame(int arg) {
    if (arg == 0) {
      // Piranhas
      board[0][0] = 2;
      board[2][11] = 2;
      board[8][2] = 2;

      // Land
      board[1][0] = 1;
      board[2][0] = 1;
      board[3][0] = 1;
      board[4][0] = 1;
      board[4][1] = 1;
      board[5][0] = 1;
      board[6][0] = 3;
      board[4][4] = 1;

      // Empty
      board[0][13] = 0;
      board[7][12] = 0;
      board[8][13] = 0;
      board[10][8] = 0;

      // Current Player
      currentPlayer = false;
      currentCard = false;

      // Lower and Upper Cards
      for (int i = 0; i < 12; i++) {
        lowerCards[i] = true;
        upperCards[i] = true;
      }
      lowerCards[0] = false;
      lowerCards[2] = false;
      upperCards[1] = false;

      // Stones
      stones[0] = 0;
      stones[1] = 0;

      // Piranhas
      piranhas[0] = 2;
      piranhas[1] = 2;

      // Create Board
      gameBoard =
          new Board(
              board,
              playCards,
              stones,
              piranhas,
              wins,
              currentPlayer,
              currentCard,
              false,
              lowerCards,
              upperCards);
    } else if (arg == 1) {
      // Piranhas
      board[0][0] = 2;
      board[2][11] = 2;
      board[8][2] = 2;

      // Land
      board[1][0] = 1;
      board[2][0] = 1;
      board[3][0] = 1;
      board[4][0] = 1;
      board[4][1] = 1;
      board[5][0] = 1;
      board[6][0] = 1;
      board[4][4] = 1;
      board[7][0] = 3;

      // Empty
      board[0][13] = 0;
      board[7][12] = 0;
      board[8][13] = 0;
      board[10][8] = 0;

      // Current Player
      currentPlayer = false;
      currentCard = false;

      // Lower and Upper Cards
      for (int i = 0; i < 12; i++) {
        lowerCards[i] = true;
        upperCards[i] = true;
      }
      lowerCards[0] = false;
      lowerCards[2] = false;
      upperCards[1] = false;

      // Stones
      stones[0] = 0;
      stones[1] = 0;

      // Piranhas
      piranhas[0] = 2;
      piranhas[1] = 2;

      // Create Board
      gameBoard =
          new Board(
              board,
              playCards,
              stones,
              piranhas,
              wins,
              currentPlayer,
              currentCard,
              false,
              lowerCards,
              upperCards);
    }
  }

  public void setPlayCards(int direction, int distance, String name) {
    int index;
    int i = direction * 3 + distance - 1;
    if (name.equals("VALENTIN")) {
      if (!gameBoard.getLowerCards()[i]
          && (firstPlayer || !systemBased)
          && ((gameBoard.getPlayCards()[0] == -1)
              || (!gameBoard.getCurrentPlayer() && (gameBoard.getPlayCards()[2] == -1)))) {
        index = (gameBoard.getPlayCards()[0] == -1) ? 0 : 2;
        gameBoard.setLowerCards(i, !gameBoard.getLowerCards()[i]);
        gameBoard.setPlayCard(index, i);
        game.updatePlayCards(i, index, true);
      }
    } else if (name.equals("GREGOR")) {
      if (!gameBoard.getUpperCards()[i]
          && (secondPlayer || !systemBased)
          && ((gameBoard.getPlayCards()[1] == -1)
              || (gameBoard.getCurrentPlayer() && (gameBoard.getPlayCards()[2] == -1)))) {
        index = (gameBoard.getPlayCards()[1] == -1) ? 1 : 2;
        gameBoard.setUpperCards(i, !gameBoard.getUpperCards()[i]);
        gameBoard.setPlayCard(index, i);
        game.updatePlayCards(i, index, false);
      }
    }
  }

  public void setPiranhaPrompt(boolean currentCard) {
    gameBoard.setCurrentCard(currentCard);
    gameBoard.setChoosePiranha(true);
  }

  public void setPiranha(int x, int y) {
    gameBoard.setBoard(y, x, 0);
    int index = gameBoard.getCurrentCard() ? 1 : 0;
    gameBoard.setPiranhas(1, gameBoard.getPiranhas()[1] + 1);
    game.updatePiranha(1, gameBoard.getPiranhas()[1], y, x, gameCreated);
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
      paintChooseFileBased(g);
      paintChooseNetwork(g);
      paintNetwork(g);
      paintChooseAI(g);
      fileGregor.readPiranha();
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

  private void paintChooseFileBased(Graphics2D g) {
    if (chooseFileBased) {
      paintChoosing(g, "    System", "     File");
    }
  }

  public static void writePlayCards(int index, boolean upperPlayer, Board gameBoard) {
    if (!systemBased) {
      if (upperPlayer) {
        if (index == 1 && gameBoard.getCurrentPlayer()) {
          System.out.print("");
        } else if (index == 2 && gameBoard.getCurrentPlayer()) {
          int[] tmp = gameBoard.getPlayCards();
          fileGregor.writePlayCards(tmp[1] / 3, (tmp[1] % 3) + 1, tmp[2] / 3, (tmp[2] % 3) + 1);
          fileValentin.readPlayCards();
        } else {
          int[] tmp = gameBoard.getPlayCards();
          fileGregor.writePlayCards(tmp[1] / 3, (tmp[1] % 3) + 1, -1, -1);
          fileValentin.readPlayCards();
        }
      } else {
        if (index == 0 && !gameBoard.getCurrentPlayer()) {
          System.out.print("");
        } else if (index == 2 && !gameBoard.getCurrentPlayer()) {
          int[] tmp = gameBoard.getPlayCards();
          fileValentin.writePlayCards(tmp[0] / 3, (tmp[0] % 3) + 1, tmp[2] / 3, (tmp[2] % 3) + 1);
          fileGregor.readPlayCards();
        } else {
          int[] tmp = gameBoard.getPlayCards();
          fileValentin.writePlayCards(tmp[0] / 3, (tmp[0] % 3) + 1, -1, -1);
          fileGregor.readPlayCards();
        }
      }
    }
  }

  public TxtHandler getFileGregor() {
    return fileGregor;
  }

  public TxtHandler getFileValentin() {
    return fileValentin;
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
            && (secondPlayer || !systemBased)
            && ((playCards2[1] == -1) || (gameBoard.getCurrentPlayer() && (playCards2[2] == -1)))) {
          int index = (playCards2[1] == -1) ? 1 : 2;
          gameBoard.setUpperCards(i, !gameBoard.getUpperCards()[i]);
          gameBoard.setPlayCard(index, i);
          game.updatePlayCards(i, index, false);
          writePlayCards(index, true, gameBoard);
        }
      } else if (x >= (cardXOffset + i * (80 + 3))
          && x <= (cardXOffset + i * (80 + 3) + 80)
          && y >= cardYOffsetBottom
          && y <= (cardYOffsetBottom + 80)) {
        if (!gameBoard.getLowerCards()[i]
            && (firstPlayer || !systemBased)
            && ((playCards2[0] == -1)
                || (!gameBoard.getCurrentPlayer() && (playCards2[2] == -1)))) {
          int index = (playCards2[0] == -1) ? 0 : 2;
          gameBoard.setLowerCards(i, !gameBoard.getLowerCards()[i]);
          gameBoard.setPlayCard(index, i);
          game.updatePlayCards(i, index, true);
          writePlayCards(index, false, gameBoard);
        }
      }
    }
    if (gameBoard.getChoosePiranha()
        && (gameBoard.getCurrentCard() != (firstPlayer || !systemBased))) {
      int[][] board2 = gameBoard.getBoard();
      for (int i = 0; i < board2.length; i++) {
        for (int j = 0; j < board2[i].length; j++) {
          if (board2[i][j] == 2
              && x >= (boardXOffset + j * 40)
              && x < (boardXOffset + (j + 1) * 40)
              && y >= (boardYOffset + i * 40)
              && y < (boardYOffset + (i + 1) * 40)) {
            gameBoard.setBoard(i, j, 0);
            int index = gameBoard.getCurrentCard() ? 1 : 0;
            gameBoard.setPiranhas(index, gameBoard.getPiranhas()[index] + 1);
            game.updatePiranha(index, gameBoard.getPiranhas()[index], i, j, gameCreated);
            if (!systemBased) {
              if (index == 0) {
                fileValentin.writePiranha(i, j);
              } else {
                fileGregor.writePiranha(i, j);
              }
            }
            repaint();
          }
        }
      }
    }
    if (chooseFileBased) {
      // System Based
      if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= (getHeight() - 45))
          && (y < (getHeight() - 5)))) {
        chooseNetwork = true;
        firstPlayer = false;
        secondPlayer = false;
        choosePlayer = false;
        chooseAI = false;
        singleplayer = false;
        chooseFileBased = false;
        systemBased = true;
        // File Based
      } else if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= 5)
          && (y < 45))) {
        chooseNetwork = false;
        firstPlayer = true;
        secondPlayer = false;
        choosePlayer = false;
        chooseAI = false;
        singleplayer = true;
        chooseFileBased = false;
        systemBased = false;
        createSingleplayerGame();
        game.setAgent(0, 2, true);
      }
      repaint();
      return;
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
        createSingleplayerGame();
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
    // TODO Change AI
    if (singleplayer && chooseAI) {
      game.setAgent(1, 2, gameCreated);
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
        game.setAgent(0, 0, gameCreated);
        // Player
      } else if (((x >= (750 + insets.left))
          && (x < (950 + insets.left))
          && (y >= 5)
          && (y < 45))) {
        chooseAI = false;
        activeAI = false;
        firstPlayer = singleplayer || gameCreated;
        secondPlayer = !singleplayer && !gameCreated;
      }
      repaint();
    }
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
