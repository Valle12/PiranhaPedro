package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import main.Launcher;

import java.io.IOException;

public class MenuController {
    private Scene singleplayerMenuScene;

    public MenuController() throws IOException {
        singleplayerMenuScene = new Scene(new FXMLLoader(this.getClass().getResource("/views/singleplayerMenuView.fxml")).load(), 1920, 1080);
    }

    public void switchToSingleplayer(ActionEvent e) {
        Launcher.mainStage.setScene(singleplayerMenuScene);
    }
}