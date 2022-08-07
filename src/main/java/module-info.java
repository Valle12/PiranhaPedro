module piranhapedro {
    requires javafx.controls;
    requires javafx.fxml;


    opens main to javafx.fxml;
    exports main;
    exports gui;
    opens gui to javafx.fxml;
}