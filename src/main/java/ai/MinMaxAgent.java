package ai;

import ft.Node;
import game.Board;
import game.Gameplay;
import game.SimulateTurn;

public class MinMaxAgent extends Agent {
  private int wins;
  private int indexMax;
  private final int depth = 9; // 1 worked best so far against random agent
  // 1 worked best so far against baseline agent

  public MinMaxAgent(Gameplay game, int playerNumber) {
    super(game, playerNumber);
  }

  @Override
  public void think() {
    Board board = game.getGameBoard();
    wins = board.getWins()[0] + board.getWins()[1];
    Node root = new Node(board, -1);
    if (board.getCurrentPlayer()) {
      if (playerNumber == 1) {
        minmax(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, root.getBoard().getCurrentCard());
        board.setUpperCards(indexMax, true);
        board.setPlayCard(1, indexMax);
        SimulateTurn sim = new SimulateTurn(board);
        sim.playTurn(indexMax);
        Node node = new Node(sim.getGameBoard(), indexMax);
        minmax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, node.getBoard().getCurrentCard());
        sim.playTurn(indexMax);
        node = new Node(sim.getGameBoard(), indexMax);
        minmax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, node.getBoard().getCurrentCard());
        board.setUpperCards(indexMax, true);
        board.setPlayCard(2, indexMax);
      } else {
        SimulateTurn sim = new SimulateTurn(board);
        minmax(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, root.getBoard().getCurrentCard());
        sim.playTurn(indexMax);
        Node node = new Node(sim.getGameBoard(), indexMax);
        minmax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, node.getBoard().getCurrentCard());
        board.setLowerCards(indexMax, true);
        board.setPlayCard(0, indexMax);
      }
    } else {
      if (playerNumber == 1) {
        SimulateTurn sim = new SimulateTurn(board);
        minmax(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, root.getBoard().getCurrentCard());
        sim.playTurn(indexMax);
        Node node = new Node(sim.getGameBoard(), indexMax);
        minmax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, node.getBoard().getCurrentCard());
        board.setUpperCards(indexMax, true);
        board.setPlayCard(1, indexMax);
      } else {
        minmax(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, root.getBoard().getCurrentCard());
        board.setLowerCards(indexMax, true);
        board.setPlayCard(0, indexMax);
        SimulateTurn sim = new SimulateTurn(board);
        sim.playTurn(indexMax);
        Node node = new Node(sim.getGameBoard(), indexMax);
        minmax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, node.getBoard().getCurrentCard());
        sim.playTurn(indexMax);
        node = new Node(sim.getGameBoard(), indexMax);
        minmax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, node.getBoard().getCurrentCard());
        board.setLowerCards(indexMax, true);
        board.setPlayCard(2, indexMax);
      }
    }
  }

  private int minmax(Node parentNode, int depth, int alpha, int beta, boolean maximizingPlayer) {
    if ((depth == 0)
        || ((parentNode.getBoard().getWins()[0] + parentNode.getBoard().getWins()[1]) > wins)) {
      return heuristics(parentNode.getBoard());
    }
    parentNode.expand();
    if (maximizingPlayer) {
      int maxEval = Integer.MIN_VALUE;
      int eval;
      for (int i = 0; i < parentNode.getChildren().size(); i++) {
        eval = minmax(parentNode.getChildren().get(i), depth - 1, alpha, beta, false);
        maxEval = Math.max(maxEval, eval);
        if (maxEval == eval && depth == this.depth) {
          indexMax = parentNode.getChildren().get(i).getIndex();
        }
        alpha = Math.max(alpha, eval);
        if (beta <= alpha) {
          break;
        }
      }
      parentNode.getChildren().clear();
      return maxEval;
    } else {
      int minEval = Integer.MAX_VALUE;
      int eval;
      for (int i = 0; i < parentNode.getChildren().size(); i++) {
        eval = minmax(parentNode.getChildren().get(i), depth - 1, alpha, beta, true);
        minEval = Math.min(minEval, eval);
        if (minEval == eval && depth == this.depth) {
          indexMax = parentNode.getChildren().get(i).getIndex();
        }
        beta = Math.min(beta, eval);
        if (beta <= alpha) {
          break;
        }
      }
      parentNode.getChildren().clear();
      return minEval;
    }
  }

  private int heuristics(Board board) {
    int piranhaDifference;
    int stoneDifference;
    int winDifference;
    int cardDifference = 0;
    if (playerNumber == 1) {
      piranhaDifference = board.getPiranhas()[0] - board.getPiranhas()[1];
      stoneDifference = board.getStones()[1] - board.getStones()[0];
      winDifference = board.getWins()[1] - board.getWins()[0];
      for (int i = 0; i < 12; i++) {
        if (!board.getUpperCards()[i]) {
          switch (i % 3) {
            case 0:
              cardDifference += 2;
              break;
            case 1:
              cardDifference++;
              break;
            default:
              break;
          }
        }
      }
      for (int i = 0; i < 12; i++) {
        if (!board.getLowerCards()[i]) {
          switch (i % 3) {
            case 0:
              cardDifference -= 2;
              break;
            case 1:
              cardDifference--;
              break;
            default:
              break;
          }
        }
      }
      int result =
          piranhaDifference * 4
              + stoneDifference * 3
              + winDifference * 50
              + cardDifference * 2
              + heuristicsWaterFields(board) * 3;
      return result;
    } else {
      piranhaDifference = board.getPiranhas()[1] - board.getPiranhas()[0];
      stoneDifference = board.getStones()[0] - board.getPiranhas()[1];
      winDifference = board.getWins()[0] - board.getWins()[1];
      for (int i = 0; i < 12; i++) {
        if (!board.getLowerCards()[i]) {
          switch (i % 3) {
            case 0:
              cardDifference += 2;
              break;
            case 1:
              cardDifference++;
              break;
            default:
              break;
          }
        }
      }
      for (int i = 0; i < 12; i++) {
        if (!board.getUpperCards()[i]) {
          switch (i % 3) {
            case 0:
              cardDifference -= 2;
              break;
            case 1:
              cardDifference--;
              break;
            default:
              break;
          }
        }
      }
      int result =
          piranhaDifference * 4
              + stoneDifference * 3
              + winDifference * 50
              + cardDifference * 2
              + heuristicsWaterFields(board) * 3;
      return result;
    }
  }

  private int heuristicsWaterFields(Board board) {
    int[][] boardArray = board.getBoard();
    int pedroI = -1;
    int pedroJ = -1;
    int counter = 0;
    A:
    for (int i = 0; i < boardArray.length; i++) {
      for (int j = 0; j < boardArray[i].length; j++) {
        if (boardArray[i][j] == 3) {
          pedroI = i;
          pedroJ = j;
          break A;
        }
      }
    }
    if (((pedroI - 1) >= 0) && (boardArray[pedroI - 1][pedroJ] == 0)) {
      counter++;
    }
    if (((pedroI + 1) < 11) && (boardArray[pedroI + 1][pedroJ] == 0)) {
      counter++;
    }
    if (((pedroJ - 1) >= 0) && (boardArray[pedroI][pedroJ - 1] == 0)) {
      counter++;
    }
    if (((pedroJ + 1) < 15) && (boardArray[pedroI][pedroJ + 1] == 0)) {
      counter++;
    }
    return counter;
  }
}
