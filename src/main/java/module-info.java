module com.examplemonevo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jdi;

    opens com.examplemonevo.Controllers to javafx.fxml;
    exports com.examplemonevo;
}
