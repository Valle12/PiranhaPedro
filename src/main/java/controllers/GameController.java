package controllers;

import game.Cell;
import game.Game;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import misc.Turn;

@Slf4j
public class GameController {
  @FXML AnchorPane anchorPaneRoot;
  @FXML Button buttonUp1;
  @FXML Button buttonUp2;
  @FXML Button buttonUp3;
  @FXML Button buttonRight1;
  @FXML Button buttonRight2;
  @FXML Button buttonRight3;
  @FXML Button buttonDown1;
  @FXML Button buttonDown2;
  @FXML Button buttonDown3;
  @FXML Button buttonLeft1;
  @FXML Button buttonLeft2;
  @FXML Button buttonLeft3;
  @FXML Label labelStones1;
  @FXML Label labelPiranhas1;
  @FXML Label labelWins1;
  @FXML Label labelStones2;
  @FXML Label labelPiranhas2;
  @FXML Label labelWins2;
  @FXML GridPane gridPaneField;
  @FXML Pane panePedro1;
  @FXML Pane panePedro2;

  private Game game;
  private List<Button> buttons;

  public GameController() {
    ControllerLibrary.setGameController(this);
  }

  public void initController() {
    // focus should not be on the buttons
    anchorPaneRoot.requestFocus();

    game = new Game();
    game.addGameListener(this::changeDisabledButtonState);
    game.addBoardListener(this::changeBoardConfiguration);
    game.addActivePlayerListener(this::changeActivePlayer);
    game.initGame();

    initGridPane();
    initButtonActions();

    game.initBoard();
  }

  private void changeActivePlayer(boolean activePlayer) {
    panePedro1.setVisible(activePlayer);
    panePedro2.setVisible(!activePlayer);
  }

  private void changeBoardConfiguration(Cell[][] board, Node[][] nodes) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        switch (board[i][j].getCellState()) {
          case WATER -> nodes[i][j].setStyle("-fx-background-color: blue; -fx-border-color: black; -fx-border-width: 0.5");
          case PIRANHA -> nodes[i][j].setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 0.5");
          case SAND -> nodes[i][j].setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 0.5");
          case PEDRO -> nodes[i][j].setStyle("-fx-background-color: lime; -fx-border-color: black; -fx-border-width: 0.5");
          case STONE -> nodes[i][j].setStyle("-fx-background-color: gray; -fx-border-color: black; -fx-border-width: 0.5");
        }
      }
    }
  }

  private void changeDisabledButtonState(boolean[] disabledButtons) {
    for (int i = 0; i < buttons.size(); i++) {
      buttons.get(i).setDisable(disabledButtons[i]);
    }
    anchorPaneRoot.requestFocus();
  }

  private void initGridPane() {
    for (int i = 0; i < gridPaneField.getColumnCount(); i++) {
      for (int j = 0; j < gridPaneField.getRowCount(); j++) {
        Pane pane = new Pane();
        gridPaneField.add(pane, i, j);
        game.addNodeToBoard(pane, i, j);
      }
    }
    logger.info("GridPane is initialized");
  }

  private void initButtonActions() {
    buttonUp1.setOnAction(e -> game.addTurn(Turn.UP1));
    buttonUp2.setOnAction(e -> game.addTurn(Turn.UP2));
    buttonUp3.setOnAction(e -> game.addTurn(Turn.UP3));
    buttonRight1.setOnAction(e -> game.addTurn(Turn.RIGHT1));
    buttonRight2.setOnAction(e -> game.addTurn(Turn.RIGHT2));
    buttonRight3.setOnAction(e -> game.addTurn(Turn.RIGHT3));
    buttonDown1.setOnAction(e -> game.addTurn(Turn.DOWN1));
    buttonDown2.setOnAction(e -> game.addTurn(Turn.DOWN2));
    buttonDown3.setOnAction(e -> game.addTurn(Turn.DOWN3));
    buttonLeft1.setOnAction(e -> game.addTurn(Turn.LEFT1));
    buttonLeft2.setOnAction(e -> game.addTurn(Turn.LEFT2));
    buttonLeft3.setOnAction(e -> game.addTurn(Turn.LEFT3));

    buttons =
        List.of(
            buttonUp1,
            buttonUp2,
            buttonUp3,
            buttonRight1,
            buttonRight2,
            buttonRight3,
            buttonDown1,
            buttonDown2,
            buttonDown3,
            buttonLeft1,
            buttonLeft2,
            buttonLeft3);

    logger.info("Button Actions are initialized");
  }
}
