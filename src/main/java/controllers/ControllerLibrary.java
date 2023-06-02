package controllers;

public class ControllerLibrary {

    private ControllerLibrary() {}
    private static GameController gameController;

    public static void setGameController(GameController gameController) {
        ControllerLibrary.gameController = gameController;
    }

    public static GameController getGameController() {
        return gameController;
    }
}
