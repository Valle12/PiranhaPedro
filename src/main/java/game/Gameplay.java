package game;

import net.messages.UpdatePlayCardsMessage;
import net.server.Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Gameplay extends Thread {
  private boolean running = true;
  private Server server;
  private Board gameBoard;
  private BufferedWriter bw;
  private int round = 1;
  private int gameNumber = 1;
  private File games;

  public Gameplay(Server server, Board gameBoard) {
    this.server = server;
    this.gameBoard = gameBoard;
    games = new File(System.getProperty("user.dir") + "/test/games.txt");
    this.start();
  }

  public Board getGameBoard() {
    return gameBoard;
  }

  private void changeCurrentPlayer() {
    gameBoard.setCurrentPlayer(!gameBoard.getCurrentPlayer());
    gameBoard.setCurrentCard(gameBoard.getCurrentPlayer());
  }

  private void resetBoard() {
    gameBoard.setPlayCard(0, -1);
    gameBoard.setPlayCard(1, -1);
    gameBoard.setPlayCard(2, -1);
    gameBoard.setStones(0, getRemainingStones(true));
    gameBoard.setStones(1, getRemainingStones(false));
    gameBoard.resetLowerAndUpperCards();
  }

  private int getRemainingStones(boolean firstPlayer) {
    int numberOfStones = 0;
    int numberOfTwos = 0;
    for (int i = 0; i < 12; i++) {
      if (firstPlayer ? !gameBoard.getLowerCards()[i] : !gameBoard.getUpperCards()[i]) {
        if ((i % 3) == 0) {
          numberOfStones++;
        } else if ((i % 3) == 1) {
          numberOfTwos++;
          if (numberOfTwos == 2) {
            numberOfTwos = 0;
            numberOfStones++;
          }
        }
      }
    }
    return numberOfStones;
  }

  private void changeCurrentCard() {
    gameBoard.setCurrentCard(!gameBoard.getCurrentCard());
  }

  public void setPlayCard(int index, int value) {
    gameBoard.setPlayCard(index, value);
  }

  private void changePedro(int pedroI, int pedroJ, int nextI, int nextJ) {
    gameBoard.setBoard(pedroI, pedroJ, 1);
    gameBoard.setBoard(nextI, nextJ, 3);
  }

  private void choosePiranha() {
    gameBoard.setChoosePiranha(true);
  }

  private boolean createLand(int pedroI, int pedroJ, int nextI, int nextJ) {
    int index = gameBoard.getCurrentCard() ? 1 : 0;
    if (gameBoard.getStones()[index] > 0) {
      gameBoard.setStones(index, gameBoard.getStones()[index] - 1);
      changePedro(pedroI, pedroJ, nextI, nextJ);
      return true;
    } else {
      if (gameBoard.getCurrentCard() && (server.getAi2() != null)) {
        server.getAi2().choosePiranha();
      } else if (!gameBoard.getCurrentCard() && (server.getAi1() != null)) {
        server.getAi1().choosePiranha();
      } else {
        choosePiranha();
      }
      return false;
    }
  }

  private void gameOver(boolean firstPlayer) {
    int index = firstPlayer ? 1 : 0;
    gameBoard.setWins(index, gameBoard.getWins()[index] + 1);
    gameBoard.setChoosePiranha(false);
    resetGame();
    if ((gameBoard.getWins()[0] == 10000) || (gameBoard.getWins()[1] == 10000)) {
      running = false;
    }
    writePiranhaToFile(
        "Player "
            + index
            + " won the game. He now has "
            + gameBoard.getWins()[index]
            + " Wins. ("
            + gameBoard.getWins()[0]
            + ","
            + gameBoard.getWins()[1]
            + ")"
            + (running ? "\n\nGame " + (gameNumber + 1) : ""));
    round = 1;
    gameNumber++;
  }

  private void resetGame() {
    gameBoard.nullifyBoard();
    gameBoard.predefineBoard();
    gameBoard.resetLowerAndUpperCards();
    gameBoard.setPlayCard(0, -1);
    gameBoard.setPlayCard(1, -1);
    gameBoard.setPlayCard(2, -1);
    gameBoard.setStones(0, 4);
    gameBoard.setStones(1, 4);
    gameBoard.setPiranhas(0, 0);
    gameBoard.setPiranhas(1, 0);
    // gameBoard.setCurrentPlayer(!gameBoard.getCurrentPlayer());
    gameBoard.setCurrentCard(gameBoard.getCurrentPlayer());
    server.repaintBoard();
  }

  public void setCard(int index, boolean firstPlayer) {
    if (firstPlayer) {
      gameBoard.setLowerCards(index, !gameBoard.getLowerCards()[index]);
    } else {
      gameBoard.setUpperCards(index, !gameBoard.getUpperCards()[index]);
    }
  }

  public void setBoard(int i, int j) {
    gameBoard.setBoard(i, j, 0);
  }

  public boolean setPiranhas(int index, int value) {
    writePiranhaToFile(
        "Player "
            + index
            + " has received a Piranha. He now has "
            + value
            + " Piranhas. ("
            + (gameBoard.getPiranhas()[0] + (index == 0 ? 1 : 0))
            + ","
            + (gameBoard.getPiranhas()[1] + (index == 1 ? 1 : 0))
            + ")");
    if (value == 3) {
      gameOver(index == 0);
      return false;
    } else {
      gameBoard.setChoosePiranha(false);
      gameBoard.setPiranhas(index, value);
      // changeCurrentPlayer();
      return true;
    }
  }

  public void writePiranhaToFile(String content) {
    try {
      bw = new BufferedWriter(new FileWriter(games, true));
      bw.write(content + "\n");
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean playTurn() {
    int pedroI = -1;
    int pedroJ = -1;
    int nextI = -1, nextJ = -1, walkLength;
    boolean forSuccessful = true;
    int playCards[] = gameBoard.getPlayCards();
    int index = 1;
    for (int i = 0; i < 3; i++) {
      int board[][] = gameBoard.getBoard();
      for (int j = 0; j < board.length; j++) {
        for (int k = 0; k < board[i].length; k++) {
          if (board[j][k] == 3) {
            pedroI = j;
            pedroJ = k;
            break;
          }
        }
      }
      walkLength = playCards[gameBoard.getCurrentPlayer() ? index : i] % 3;
      switch (playCards[gameBoard.getCurrentPlayer() ? index : i] / 3) {
        case 0:
          nextI = pedroI - walkLength - 1;
          forSuccessful = forDifferent(board, false, nextI - pedroI, pedroI, pedroJ);
          break;
        case 1:
          nextJ = pedroJ + walkLength + 1;
          forSuccessful = forDifferent(board, true, nextJ - pedroJ, pedroI, pedroJ);
          break;
        case 2:
          nextI = pedroI + walkLength + 1;
          forSuccessful = forDifferent(board, false, nextI - pedroI, pedroI, pedroJ);
          break;
        case 3:
          nextJ = pedroJ - walkLength - 1;
          forSuccessful = forDifferent(board, true, nextJ - pedroJ, pedroI, pedroJ);
          break;
      }
      if (forSuccessful) {
        changeCurrentCard();
        setPlayCard(gameBoard.getCurrentPlayer() ? index : i, -1);
        index = (index + 2) % 3;
      } else {
        return false;
      }
    }
    return true;
  }

  private boolean forDifferent(
      int[][] board, boolean change, int walkLength, int pedroI, int pedroJ) {
    int nextI = pedroI;
    int nextJ = pedroJ;
    for (int i = 0; i < Math.abs(walkLength); i++) {
      if ((walkLength >= 0)
          ? ((change ? (nextJ = pedroJ + 1) : (nextI = pedroI + 1)) < (change ? 15 : 11))
          : ((change ? (nextJ = pedroJ - 1) : (nextI = pedroI - 1)) >= 0)) {
        if (board[nextI][nextJ] == 1) {
          changePedro(pedroI, pedroJ, nextI, nextJ);
          if (change) {
            pedroJ = nextJ;
          } else {
            pedroI = nextI;
          }
        } else if (board[nextI][nextJ] == 2) {
          if (gameBoard.getCurrentCard() && (server.getAi2() != null)) {
            server.getAi2().choosePiranha();
          } else if (!gameBoard.getCurrentCard() && (server.getAi1() != null)) {
            server.getAi1().choosePiranha();
          } else {
            choosePiranha();
          }
          return false;
        } else if (board[nextI][nextJ] == 0) {
          if (createLand(pedroI, pedroJ, nextI, nextJ)) {
            if (change) {
              pedroJ = nextJ;
            } else {
              pedroI = nextI;
            }
          } else {
            return false;
          }
        }
      } else {
        if (gameBoard.getCurrentCard() && (server.getAi2() != null)) {
          server.getAi2().choosePiranha();
        } else if (!gameBoard.getCurrentCard() && (server.getAi1() != null)) {
          server.getAi1().choosePiranha();
        } else {
          choosePiranha();
        }
        return false;
      }
    }
    return true;
  }

  public Server getServer() {
    return server;
  }

  public void run() {
    try {
      if (games.exists()) {
        games.delete();
      }
      games.createNewFile();
      bw = new BufferedWriter(new FileWriter(games, true));
      bw.write("Game " + gameNumber + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (running) {
      try {
        sleep(0);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (!Frame.systemBased) {
        Frame.fileGregor.readPlayCards();
      }
      if ((server.getAi1() != null) && (gameBoard.getPlayCards()[0] == -1)) {
        server.getAi1().think();
        if (gameBoard.getCurrentPlayer()) {
          server.sendToAll(new UpdatePlayCardsMessage(gameBoard.getPlayCards()[0], 0, true));
          System.out.println("GAMEPLAY: " + gameBoard.getPlayCards()[0]);
        } else {
          server.sendToAll(new UpdatePlayCardsMessage(gameBoard.getPlayCards()[0], 0, true));
          server.sendToAll(new UpdatePlayCardsMessage(gameBoard.getPlayCards()[2], 2, true));
          System.out.println("GAMEPLAY: " + gameBoard.getPlayCards()[0]);
          System.out.println("GAMEPLAY: " + gameBoard.getPlayCards()[2]);
        }
      }
      if ((server.getAi2() != null) && (gameBoard.getPlayCards()[1] == -1)) {
        server.getAi2().think();
        if (gameBoard.getCurrentPlayer()) {
          server.sendToAll(new UpdatePlayCardsMessage(gameBoard.getPlayCards()[1], 1, false));
          server.sendToAll(new UpdatePlayCardsMessage(gameBoard.getPlayCards()[2], 2, false));
        } else {
          server.sendToAll(new UpdatePlayCardsMessage(gameBoard.getPlayCards()[1], 1, false));
        }
      }
      boolean playTurnSuccessful = true;
      int[] playCards = gameBoard.getPlayCards();
      if ((playCards[0] != -1) && (playCards[1] != -1) && (playCards[2] != -1)) {
        writePiranhaToFile(
            "PlayCards in Round "
                + round++
                + ": "
                + Arrays.toString(playCards)
                + ", Current Player: "
                + (gameBoard.getCurrentPlayer() ? "Top." : "Bottom."));
        playTurnSuccessful = playTurn();
        boolean[] lowerCards = gameBoard.getLowerCards();
        boolean methodValue = true;
        for (int i = 0; i < lowerCards.length; i++) {
          methodValue = methodValue && lowerCards[i];
        }
        // TODO HANDLE WITH CARE
        changeCurrentPlayer();
        if (playTurnSuccessful) {
          // TODO WAS HERE
        } else {
          resetBoard();
        }
        if (methodValue) {
          gameBoard.resetLowerAndUpperCards();
        }
        server.repaintBoard();
      }
    }
  }
}
