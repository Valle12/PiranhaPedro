package ai;

import game.Board;
import game.Gameplay;

public class RandomAgent extends Agent {

  public RandomAgent(Gameplay game, int playerNumber) {
    super(game, playerNumber);
  }

  @Override
  public void think() {
    Board board = game.getGameBoard();
    if (board.getCurrentPlayer()) {
      if (playerNumber == 1) {
        int random, counter = 0;
        A: while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getUpperCards()[random]) {
            board.setUpperCards(random, !board.getUpperCards()[random]);
            board.setPlayCard(counter + 1, random);
            counter++;
            if (counter == 2) {
              break A;
            }
          }
        }
      } else {
        int random;
        A: while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getLowerCards()[random]) {
            board.setLowerCards(random, !board.getLowerCards()[random]);
            board.setPlayCard(0, random);
            break A;
          }
        }
      }
    } else {
      if (playerNumber == 1) {
        int random;
        A: while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getUpperCards()[random]) {
            board.setUpperCards(random, !board.getUpperCards()[random]);
            board.setPlayCard(1, random);
            break A;
          }
        }
      } else {
        int random, counter = 0;
        A: while (true) {
          random = (int) (Math.random() * 12);
          if (!board.getLowerCards()[random]) {
            board.setLowerCards(random, !board.getLowerCards()[random]);
            board.setPlayCard(counter, random);
            counter += 2;
            if (counter == 4) {
              break A;
            }
          }
        }
      }
    }
  }
}
