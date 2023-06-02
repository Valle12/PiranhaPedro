package repository;

import javafx.stage.Stage;

public class MasterRepository {
  private static Stage mainStage;

  private MasterRepository() {}

  public static Stage getMainStage() {
    return mainStage;
  }

  public static void setMainStage(Stage mainStage) {
    MasterRepository.mainStage = mainStage;
  }
}
