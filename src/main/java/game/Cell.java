package game;

import lombok.AllArgsConstructor;
import lombok.Data;
import misc.CellState;

@Data
@AllArgsConstructor
public class Cell {
    private CellState cellState;
}
