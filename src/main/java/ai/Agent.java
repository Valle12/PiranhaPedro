package ai;

import game.Gameplay;
import net.messages.UpdatePiranhaMessage;

public abstract class Agent {
  protected Gameplay game;
  protected int playerNumber;

  public Agent(Gameplay game, int playerNumber) {
    this.game = game;
    this.playerNumber = playerNumber;
  }

  public abstract void think();

  /** Chooses first piranha on board */
  public void choosePiranha() {
    int[][] board = game.getGameBoard().getBoard();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 2) {
          board[i][j] = 0;
          game.setPiranhas(playerNumber, game.getGameBoard().getPiranhas()[playerNumber] + 1);
          game.getServer().sendToAll(new UpdatePiranhaMessage(playerNumber, 0, i, j));
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          return;
        }
      }
    }
  }

  public int getPlayerNumber() {
    return playerNumber;
  }
}
