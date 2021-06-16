package game;

public class SimulateTurn {
  private Board gameBoard;

  /**
   * Constructor for creating SimulateTurn instance
   *
   * @param gameBoard Requires Board that represents current State
   */
  public SimulateTurn(Board gameBoard) {
    this.gameBoard = new Board(gameBoard);
  }

  public Board getGameBoard() {
    return gameBoard;
  }

  public void playTurn(int index) {
    int pedroI = -1;
    int pedroJ = -1;
    int nextI;
    int nextJ;
    int walkLength;
    int[][] board = gameBoard.getBoard();
    A:
    for (int j = 0; j < board.length; j++) {
      for (int k = 0; k < board[j].length; k++) {
        if (board[j][k] == 3) {
          pedroI = j;
          pedroJ = k;
          break A;
        }
      }
    }
    walkLength = index % 3;
    switch (index / 3) {
      case 0:
        nextI = pedroI - walkLength - 1;
        forDifferent(board, false, nextI - pedroI, pedroI, pedroJ);
        break;
      case 1:
        nextJ = pedroJ + walkLength + 1;
        forDifferent(board, true, nextJ - pedroJ, pedroI, pedroJ);
        break;
      case 2:
        nextI = pedroI + walkLength + 1;
        forDifferent(board, false, nextI - pedroI, pedroI, pedroJ);
        break;
      case 3:
        nextJ = pedroJ - walkLength - 1;
        forDifferent(board, true, nextJ - pedroJ, pedroI, pedroJ);
        break;
    }
    gameBoard.setCurrentCard(!gameBoard.getCurrentCard());
  }

  private void forDifferent(int[][] board, boolean change, int walkLength, int pedroI, int pedroJ) {
    int nextI = pedroI;
    int nextJ = pedroJ;
    for (int i = 0; i < Math.abs(walkLength); i++) {
      if ((walkLength >= 0)
          ? ((change ? (nextJ = pedroJ + 1) : (nextI = pedroI + 1)) < (change ? 15 : 11))
          : ((change ? (nextJ = pedroJ - 1) : (nextI = pedroI - 1)) >= 0)) {
        if (board[nextI][nextJ] == 0) {
          if (createLand(pedroI, pedroJ, nextI, nextJ)) {
            if (change) {
              pedroJ = nextJ;
            } else {
              pedroI = nextI;
            }
          }
        } else if (board[nextI][nextJ] == 1) {
          changePedro(pedroI, pedroJ, nextI, nextJ);
          if (change) {
            pedroJ = nextJ;
          } else {
            pedroI = nextI;
          }
        } else {
          if (gameBoard.getCurrentCard()) {
            if ((gameBoard.getPiranhas()[1] + 1) == 3) {
              gameOver(0);
            } else {
              choosePiranha();
            }
          } else {
            if ((gameBoard.getPiranhas()[0] + 1) == 3) {
              gameOver(1);
            } else {
              choosePiranha();
            }
          }
          return;
        }
      } else {
        if (gameBoard.getCurrentCard()) {
          if ((gameBoard.getPiranhas()[1] + 1) == 3) {
            gameOver(0);
          } else {
            choosePiranha();
          }
        } else {
          if ((gameBoard.getPiranhas()[0] + 1) == 3) {
            gameOver(1);
          } else {
            choosePiranha();
          }
        }
        return;
      }
    }
  }

  private boolean createLand(int pedroI, int pedroJ, int nextI, int nextJ) {
    int index = gameBoard.getCurrentCard() ? 1 : 0;
    if (gameBoard.getStones()[index] > 0) {
      gameBoard.setStones(index, gameBoard.getStones()[index] - 1);
      changePedro(pedroI, pedroJ, nextI, nextJ);
      return true;
    } else {
      if (gameBoard.getCurrentCard()) {
        if ((gameBoard.getPiranhas()[1] + 1) == 3) {
          gameOver(0);
        } else {
          choosePiranha();
        }
      } else {
        if ((gameBoard.getPiranhas()[0] + 1) == 3) {
          gameOver(1);
        } else {
          choosePiranha();
        }
      }
      return false;
    }
  }

  private void choosePiranha() {
    int index = gameBoard.getCurrentCard() ? 1 : 0;
    int[][] board = gameBoard.getBoard();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 2) {
          board[i][j] = 0;
          resetBoard();
          gameBoard.setPiranhas(index, gameBoard.getPiranhas()[index] + 1);
          return;
        }
      }
    }
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

  private void gameOver(int index) {
    gameBoard.setWins(index, gameBoard.getWins()[index] + 1);
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
  }

  private void changePedro(int pedroI, int pedroJ, int nextI, int nextJ) {
    gameBoard.setBoard(pedroI, pedroJ, 1);
    gameBoard.setBoard(nextI, nextJ, 3);
  }
}
