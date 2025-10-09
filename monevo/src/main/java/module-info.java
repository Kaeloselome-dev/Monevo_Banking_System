module com.examplemonevo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.examplemonevo to javafx.fxml;
    exports com.examplemonevo;
}
