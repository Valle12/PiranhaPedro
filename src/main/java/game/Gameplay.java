package game;

import net.server.Server;

public class Gameplay extends Thread {
  private boolean running = true;
  private Server server;
  private Board gameBoard;

  public Gameplay(Server server, Board gameBoard) {
    this.server = server;
    this.gameBoard = gameBoard;
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
      choosePiranha();
      return false;
    }
  }

  private void gameOver(boolean firstPlayer) {
    int index = firstPlayer ? 1 : 0;
    gameBoard.setWins(index, gameBoard.getWins()[index] + 1);
    gameBoard.setChoosePiranha(false);
    resetGame();
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
    gameBoard.setCurrentPlayer(Math.random() > 0.5);
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
    if (value == 3) {
      gameOver(index == 0);
      return false;
    } else {
      gameBoard.setChoosePiranha(false);
      gameBoard.setPiranhas(index, value);
      changeCurrentPlayer();
      return true;
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
          choosePiranha();
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
        choosePiranha();
        return false;
      }
    }
    return true;
  }

  public void run() {
    while (running) {
      try {
        sleep(0);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      boolean playTurnSuccessful = true;
      int[] playCards = gameBoard.getPlayCards();
      if ((playCards[0] != -1) && (playCards[1] != -1) && (playCards[2] != -1)) {
        playTurnSuccessful = playTurn();
        boolean[] lowerCards = gameBoard.getLowerCards();
        boolean methodValue = true;
        for (int i = 0; i < lowerCards.length; i++) {
          methodValue = methodValue && lowerCards[i];
        }
        if (playTurnSuccessful) {
          changeCurrentPlayer();
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
