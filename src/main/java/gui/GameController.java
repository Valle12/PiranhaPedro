package gui;

import game.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.Main;

public class GameController {
    private Game game;

    @FXML private Label labelNumberOfStones1, labelNumberOfStones2, labelNumberOfWins1, labelNumberOfWins2;
    @FXML private Pane pane1, pane2;

    public GameController() {
        Main.gameController = this;
    }

    public void initialize() {
        // Initialize new game
        game = new Game();

        // Initialize labels
        labelNumberOfStones1.setText("Anzahl Steine: " + game.getPlayers()[0].getNumberOfStones());
        labelNumberOfStones2.setText("Anzahl Steine: " + game.getPlayers()[1].getNumberOfStones());
        labelNumberOfWins1.setText("Gewonnene Spiele: " + game.getPlayers()[0].getNumberOfWins());
        labelNumberOfWins2.setText("Gewonnene Spiele: " + game.getPlayers()[1].getNumberOfWins());

        // Initialize panes
        pane1.setStyle("-fx-background-color: " + (game.getPlayers()[0].isStartingPlayer() ? "red;" : "none;"));
        pane2.setStyle("-fx-background-color: " + (game.getPlayers()[1].isStartingPlayer() ? "red;" : "none;"));
    }
}
