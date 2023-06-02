package main;

import controllers.ControllerLibrary;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import repository.MasterRepository;

@Slf4j
public class Main extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    MasterRepository.setMainStage(stage);

    Scene scene =
        new Scene(
            FXMLLoader.load(
                Objects.requireNonNull(this.getClass().getResource("/views/gameView.fxml"))),
            1080,
            720);

    stage.setScene(scene);
    stage.setTitle("Piranha Pedro");
    stage.show();

    ControllerLibrary.getGameController().initController();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
