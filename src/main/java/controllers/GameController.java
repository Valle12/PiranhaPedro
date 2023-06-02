package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

  public GameController() {
    ControllerLibrary.setGameController(this);
  }

  public void initController() {
    anchorPaneRoot.requestFocus();
  }
}
