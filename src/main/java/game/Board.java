package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import listeners.BoardListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import misc.CellState;

@Slf4j
@Data
public class Board {
  private final Cell[][] cells = new Cell[15][11];
  private final Node[][] nodes = new Node[15][11];
  private final List<BoardListener> boardListeners = new ArrayList<>();

  public void addListener(BoardListener listener) {
    boardListeners.add(listener);
  }

  public void setNode(Node node, int i, int j) {
    nodes[i][j] = node;
  }

  public void initBoard() {
    // initialize every field with water at first
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new Cell(CellState.WATER);
      }
    }

    // initialize piranhas
    cells[0][0] = new Cell(CellState.PIRANHA);
    cells[13][0] = new Cell(CellState.PIRANHA);
    cells[11][2] = new Cell(CellState.PIRANHA);
    cells[10][7] = new Cell(CellState.PIRANHA);
    cells[2][8] = new Cell(CellState.PIRANHA);
    cells[13][8] = new Cell(CellState.PIRANHA);
    cells[8][10] = new Cell(CellState.PIRANHA);

    // initialize sand
    cells[4][2] = new Cell(CellState.SAND);
    cells[4][3] = new Cell(CellState.SAND);
    cells[5][3] = new Cell(CellState.SAND);
    cells[2][4] = new Cell(CellState.SAND);
    cells[3][4] = new Cell(CellState.SAND);
    cells[5][4] = new Cell(CellState.SAND);
    cells[4][5] = new Cell(CellState.SAND);

    // initialize pedro
    cells[4][4] = new Cell(CellState.PEDRO);

    logger.info("Cells are initialized");

    changeBoardConfiguration();
  }

  private void changeBoardConfiguration() {
    for (BoardListener listener : boardListeners) {
      listener.onBoardChange(cells, nodes);
    }
  }
}
