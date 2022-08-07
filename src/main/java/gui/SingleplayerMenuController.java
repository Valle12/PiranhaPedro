package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import main.Launcher;
import main.Main;

import java.io.IOException;

public class SingleplayerMenuController {
    private Scene gameScene;

    public void switchToGame() throws IOException {
        gameScene = new Scene(new FXMLLoader(this.getClass().getResource("/views/gameView.fxml")).load(), 1920, 1080);
        Launcher.mainStage.setScene(gameScene);
        Main.gameController.initialize();
    }
}