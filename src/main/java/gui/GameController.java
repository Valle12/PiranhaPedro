package gui;

import common.Direction;
import common.GameCell;
import common.GameField;
import common.Turn;
import game.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import main.Main;

public class GameController {

    @FXML
    private Label labelNumberOfStones1, labelNumberOfStones2, labelNumberOfWins1, labelNumberOfWins2;
    @FXML
    private Pane pane1, pane2;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPaneField;
    @FXML
    private Button buttonUp1, buttonUp2, buttonUp3, buttonRight1, buttonRight2, buttonRight3, buttonBottom1, buttonBottom2, buttonBottom3, buttonLeft1, buttonLeft2, buttonLeft3;
    private Button[] playerButtons = new Button[12];
    private Game game;
    private GameField gameField;
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

        // Initialize GameField
        gameField = new GameField();
        gameField.initializeField();

        GameCell[][] field = gameField.getField();

        // For every cell in the GridPane, add another Node inside to do something with this square
        for (int i = 0; i < gridPaneField.getRowCount(); i++) {
            for (int j = 0; j < gridPaneField.getColumnCount(); j++) {
                gridPaneField.add(new Pane(), i, j);
            }
        }

        // Iterate over all nodes inside the GridPane and color them correctly
        for (Node node: gridPaneField.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) < 11 && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) < 15) {
                if (field[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] == GameCell.WATER) {
                    node.setStyle("-fx-background-color: blue; -fx-border-color: black");
                } else if (field[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] == GameCell.PEDRO) {
                    node.setStyle("-fx-background-color: lime; -fx-border-color: black");
                } else if (field[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] == GameCell.ISLAND) {
                    node.setStyle("-fx-background-color: yellow; -fx-border-color: black");
                } else if (field[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] == GameCell.PIRANHA) {
                    node.setStyle("-fx-background-color: red; -fx-border-color: black");
                } else {
                    node.setStyle("-fx-background-color: gray; -fx-border-color: black");
                }
            }
        }

        // Initialize onAction methods
        buttonUp1.setOnAction(e -> game.addTurn(new Turn(Direction.UP, 1)));
        buttonUp2.setOnAction(e -> game.addTurn(new Turn(Direction.UP, 2)));
        buttonUp3.setOnAction(e -> game.addTurn(new Turn(Direction.UP, 3)));
        buttonRight1.setOnAction(e -> game.addTurn(new Turn(Direction.RIGHT, 1)));
        buttonRight2.setOnAction(e -> game.addTurn(new Turn(Direction.RIGHT, 2)));
        buttonRight3.setOnAction(e -> game.addTurn(new Turn(Direction.RIGHT, 3)));
        buttonBottom1.setOnAction(e -> game.addTurn(new Turn(Direction.DOWN, 1)));
        buttonBottom2.setOnAction(e -> game.addTurn(new Turn(Direction.DOWN, 2)));
        buttonBottom3.setOnAction(e -> game.addTurn(new Turn(Direction.DOWN, 3)));
        buttonLeft1.setOnAction(e -> game.addTurn(new Turn(Direction.LEFT, 1)));
        buttonLeft2.setOnAction(e -> game.addTurn(new Turn(Direction.LEFT, 2)));
        buttonLeft3.setOnAction(e -> game.addTurn(new Turn(Direction.LEFT, 3)));

        // Add all buttons to playerButtons[]
        playerButtons[0] = buttonUp1;
        playerButtons[1] = buttonUp2;
        playerButtons[2] = buttonUp3;
        playerButtons[3] = buttonRight1;
        playerButtons[4] = buttonRight2;
        playerButtons[5] = buttonRight3;
        playerButtons[6] = buttonBottom1;
        playerButtons[7] = buttonBottom2;
        playerButtons[8] = buttonBottom3;
        playerButtons[9] = buttonLeft1;
        playerButtons[10] = buttonLeft2;
        playerButtons[11] = buttonLeft3;
    }

    public void updateClickableButtons() {
        boolean[] cardsPlayed = game.getPlayers()[0].getCardsPlayed();

        for (int i = 0; i < 12; i++) {
            playerButtons[i].setDisable(cardsPlayed[i]);
        }
    }

    // Disable or enable all buttons at once
    public void changeDisabledStateAllButtons(boolean disabled) {
        for (Button button : playerButtons) {
            button.setDisable(disabled);
        }
    }
}
