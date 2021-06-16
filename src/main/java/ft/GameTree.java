package ft;

import game.Board;
import game.SimulateTurn;

public class GameTree {
  private Node root;
  private int depth;
  private int wins;
  private Board gameBoard;

  public GameTree(int depth, Board gameBoard) {
    this.depth = depth;
    this.gameBoard = gameBoard;
    this.wins = gameBoard.getWins()[0] + gameBoard.getWins()[1];
    root = new Node(gameBoard);
    createGameTree(root, this.depth);
  }

  public Node getRoot() {
    return root;
  }

  private void createGameTree(Node parentNode, int depth) {
    Board board = parentNode.getBoard();
    if (depth == 0 || ((board.getWins()[0] + board.getWins()[1]) > wins)) {
      return;
    }
    for (int i = 0; i < 12; i++) {
      if (board.getCurrentCard()) {
        if (!board.getUpperCards()[i]) {
          board.setUpperCards(i, true);
          boolean methodValue = true;
          SimulateTurn sim = new SimulateTurn(board);
          sim.playTurn(i);
          for (int j = 0; j < board.getUpperCards().length; j++) {
            methodValue = methodValue && board.getUpperCards()[j] && board.getLowerCards()[j];
            if (!methodValue) {
              break;
            }
          }
          if (methodValue) {
            board.resetLowerAndUpperCards();
          }
          board.setUpperCards(i, false);
          parentNode.addChild(new Node(sim.getGameBoard()));
        }
      } else {
        if (!board.getLowerCards()[i]) {
          board.setLowerCards(i, true);
          boolean methodValue = true;
          SimulateTurn sim = new SimulateTurn(board);
          sim.playTurn(i);
          for (int j = 0; j < board.getLowerCards().length; j++) {
            methodValue = methodValue && board.getLowerCards()[j] && board.getUpperCards()[j];
            if (!methodValue) {
              break;
            }
          }
          if (methodValue) {
            board.resetLowerAndUpperCards();
          }
          board.setLowerCards(i, false);
          parentNode.addChild(new Node(sim.getGameBoard()));
        }
      }
    }
    for (Node n : parentNode.getChildren()) {
      createGameTree(n, depth - 1);
    }
  }
}
