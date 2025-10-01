module FoundationsF25 {
    requires javafx.controls;
    requires javafx.graphics;   // <- needed for javafx.stage.Stage
    requires javafx.fxml;       // <- needed if you load FXML
    requires java.sql;          // <- if you use H2/SQL

    // If you use FXML controllers in these packages, keep them opened to FXML/graphics:
    opens applicationMain to javafx.fxml, javafx.graphics;

    // If other packages contain FXML controllers, open them too, e.g.:
    // opens guiFirstAdmin to javafx.fxml, javafx.graphics;
    // opens guiUserUpdate to javafx.fxml, javafx.graphics;

    // Export packages that other modules (or your launch config) need to see at compile time:
    exports applicationMain;
    // exports guiFirstAdmin;
    // exports guiUserUpdate;
}
