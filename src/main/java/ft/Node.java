package ft;

import game.Board;
import game.SimulateTurn;

import java.util.ArrayList;

public class Node {
  private Board board;
  private ArrayList<Node> children = new ArrayList<Node>();

  public Node(Board board) {
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }

  public ArrayList<Node> getChildren() {
    return children;
  }

  public void addChild(Node node) {
    children.add(node);
  }

  public void expand() {
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
          children.add(new Node(sim.getGameBoard()));
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
          children.add(new Node(sim.getGameBoard()));
        }
      }
    }
  }
}
