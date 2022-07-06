module piranhapedro.piranhapedro {
    requires javafx.controls;
    requires javafx.fxml;


    opens piranhapedro.piranhapedro to javafx.fxml;
    exports piranhapedro.piranhapedro;
}