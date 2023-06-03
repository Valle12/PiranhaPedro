package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import listeners.ActivePlayerListener;
import listeners.BoardListener;
import listeners.GameListener;
import lombok.extern.slf4j.Slf4j;
import misc.Turn;

@Slf4j
public class Game {
  private final Turn[] turns = new Turn[3];
  private boolean activePlayer;
  private final boolean[] disabledButtons = new boolean[12];
  private final List<GameListener> gameListeners = new ArrayList<>();
  private final List<ActivePlayerListener> activePlayerListeners = new ArrayList<>();
  private final Board board = new Board();


  public void initGame() {
    activePlayer = Math.floor(Math.random() * 2) == 0;
    changeActivePlayer();
    logger.info("Active Player is initialized");
  }

  public void addActivePlayerListener(ActivePlayerListener listener) {
    activePlayerListeners.add(listener);
  }

  public void addTurn(Turn turn) {
    if (activePlayer) {
      if (turns[0] == null) {
        turns[0] = turn;
      } else {
        turns[2] = turn;
      }
    } else {
      turns[0] = turn;
    }
    disabledButtons[turn.getButtonNumberFromTurn()] = true;
    changeDisabledButtonState();
  }

  public void addGameListener(GameListener listener) {
    gameListeners.add(listener);
  }

  public void addBoardListener(BoardListener listener) {
    board.addListener(listener);
  }

  public void addNodeToBoard(Node node, int i, int j) {
    board.setNode(node, i, j);
  }

  public void initBoard() {
    board.initBoard();
  }

  private void changeDisabledButtonState() {
    for (GameListener listener : gameListeners) {
      listener.onButtonChange(disabledButtons);
    }
  }

  private void changeActivePlayer() {
    for (ActivePlayerListener listener : activePlayerListeners) {
      listener.onPlayerChange(activePlayer);
    }
  }
}
