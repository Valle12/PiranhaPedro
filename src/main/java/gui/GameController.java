package gui;

import game.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import main.Main;

public class GameController {
    private Game game;

    @FXML
    private Label labelNumberOfStones1, labelNumberOfStones2, labelNumberOfWins1, labelNumberOfWins2;
    @FXML
    private Pane pane1, pane2;
    @FXML
    private AnchorPane anchorPane;
    private final double hboxCardButtonHeight = Main.stageHeight / 5, cardButtonHeight = Main.stageHeight / 6, middleHboxHeight = Main.stageHeight - 2 * hboxCardButtonHeight;

    public GameController() {
        Main.gameController = this;
    }

    public void initialize() {
        // Initialize the dynamic board
        anchorPane.requestFocus();
        ObservableList<Node> children = anchorPane.getChildren();

        // Set AnchorPane Constraints for middle HBox
        AnchorPane.setTopAnchor(children.get(1), hboxCardButtonHeight);
        AnchorPane.setBottomAnchor(children.get(1), hboxCardButtonHeight);

        // Set height for HBoxes
        ((HBox) children.get(0)).setPrefHeight(hboxCardButtonHeight);
        ((HBox) children.get(1)).setPrefHeight(middleHboxHeight);
        ((HBox) children.get(2)).setPrefHeight(hboxCardButtonHeight);

        // Set height for Top Card Buttons
        for (Node node : ((HBox) children.get(0)).getChildren()) {
            ((Button) node).setPrefHeight(cardButtonHeight);
        }

        // Set heights and widths for middle HBox
        AnchorPane middleHBox = (AnchorPane) ((HBox) children.get(1)).getChildren().get(0);
        AnchorPane.setLeftAnchor(middleHBox.getChildren().get(1), Main.stageWidth / 5);
        ((VBox) middleHBox.getChildren().get(0)).setPrefHeight(middleHboxHeight);
        ((VBox) middleHBox.getChildren().get(0)).setPrefWidth(Main.stageWidth / 5);
        ((GridPane) middleHBox.getChildren().get(1)).setPrefHeight(middleHboxHeight);
        ((GridPane) middleHBox.getChildren().get(1)).setPrefWidth(middleHboxHeight / 11 * 15);
        ((VBox) middleHBox.getChildren().get(2)).setPrefHeight(middleHboxHeight);
        ((VBox) middleHBox.getChildren().get(2))
                .setPrefWidth(Main.stageWidth - Main.stageWidth / 5 - middleHboxHeight / 11 * 15);
        pane1.setPrefWidth(middleHboxHeight / 2);
        pane1.setPrefHeight(middleHboxHeight / 2);
        pane2.setPrefWidth(middleHboxHeight / 2);
        pane2.setPrefHeight(middleHboxHeight / 2);

        // Set height for Bottom Card Buttons
        for (Node node : ((HBox) children.get(2)).getChildren()) {
            ((Button) node).setPrefHeight(cardButtonHeight);
        }

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
