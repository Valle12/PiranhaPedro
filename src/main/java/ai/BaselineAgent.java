package ai;

import game.Board;
import game.Gameplay;
import game.SimulateTurn;

public class BaselineAgent extends Agent {

  public BaselineAgent(Gameplay game, int playerNumber) {
    super(game, playerNumber);
  }

  @Override
  public void think() {
    Board board = game.getGameBoard();
    int maxEval = Integer.MIN_VALUE;
    int eval;
    int indexMax = 0;
    if (board.getCurrentPlayer()) {
      if (playerNumber == 1) {
        for (int j = 0; j < 12; j++) {
          if (!board.getUpperCards()[j]) {
            SimulateTurn sim = new SimulateTurn(board);
            sim.playTurn(j);
            eval = heuristics(sim.getGameBoard());
            maxEval = Math.max(maxEval, eval);
            if (maxEval == eval) {
              indexMax = j;
            }
          }
        }
        board.setUpperCards(indexMax, true);
        board.setPlayCard(1, indexMax);
        int random;
        while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getUpperCards()[random]) {
            board.setUpperCards(random, true);
            board.setPlayCard(2, random);
            break;
          }
        }
      } else {
        int random;
        while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getLowerCards()[random]) {
            board.setLowerCards(random, true);
            board.setPlayCard(0, random);
            break;
          }
        }
      }
    } else {
      if (playerNumber == 1) {
        int random;
        while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getUpperCards()[random]) {
            board.setUpperCards(random, true);
            board.setPlayCard(1, random);
            break;
          }
        }
      } else {
        for (int j = 0; j < 12; j++) {
          if (!board.getLowerCards()[j]) {
            SimulateTurn sim = new SimulateTurn(board);
            sim.playTurn(j);
            eval = heuristics(sim.getGameBoard());
            maxEval = Math.max(maxEval, eval);
            if (maxEval == eval) {
              indexMax = j;
            }
          }
        }
        board.setLowerCards(indexMax, true);
        board.setPlayCard(0, indexMax);
        int random;
        while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getLowerCards()[random]) {
            board.setLowerCards(random, true);
            board.setPlayCard(2, random);
            break;
          }
        }
      }
    }
  }

  private int heuristics(Board board) {
    int piranhaDifference;
    int stoneDifference;
    int cardDifference = 0;
    if (playerNumber == 1) {
      piranhaDifference = board.getPiranhas()[0] - board.getPiranhas()[1];
      stoneDifference = board.getStones()[1] - board.getPiranhas()[0];
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
      return (piranhaDifference * 2 + stoneDifference + cardDifference);
    } else {
      piranhaDifference = board.getPiranhas()[1] - board.getPiranhas()[0];
      stoneDifference = board.getStones()[0] - board.getPiranhas()[1];
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
      return (piranhaDifference * 2 + stoneDifference + cardDifference);
    }
  }
}
