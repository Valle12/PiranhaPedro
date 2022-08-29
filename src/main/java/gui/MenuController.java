package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import main.Launcher;
import main.Main;

import java.io.IOException;

public class MenuController {
    private Scene singleplayerMenuScene;

    public MenuController() throws IOException {
        singleplayerMenuScene = new Scene(new FXMLLoader(this.getClass().getResource("/views/singleplayerMenuView.fxml")).load(), Main.stageWidth, Main.stageHeight);
    }

    public void switchToSingleplayer(ActionEvent e) {
        Launcher.mainStage.setScene(singleplayerMenuScene);
    }
}