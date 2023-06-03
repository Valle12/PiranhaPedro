package listeners;

import game.Cell;
import javafx.scene.Node;

public interface BoardListener {
    void onBoardChange(Cell[][] board, Node[][] nodes);
}
