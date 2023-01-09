package common;

public class GameField {
    private GameCell[][] field = new GameCell[11][15];

    public void initializeField() {
        // Initialize every cell with water
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = GameCell.WATER;
            }
        }

        // Initialize the starting field of pedro
        field[4][4] = GameCell.PEDRO;

        // Initialize the island
        field[2][4] = GameCell.ISLAND;
        field[3][4] = GameCell.ISLAND;
        field[3][5] = GameCell.ISLAND;
        field[4][2] = GameCell.ISLAND;
        field[4][3] = GameCell.ISLAND;
        field[4][5] = GameCell.ISLAND;
        field[5][4] = GameCell.ISLAND;

        // Initialize the piranhas
        field[0][0] = GameCell.PIRANHA;
        field[0][13] = GameCell.PIRANHA;
        field[2][11] = GameCell.PIRANHA;
        field[7][12] = GameCell.PIRANHA;
        field[8][2] = GameCell.PIRANHA;
        field[8][13] = GameCell.PIRANHA;
        field[10][8] = GameCell.PIRANHA;
    }

    public GameCell[][] getField() {
        return field;
    }
}
