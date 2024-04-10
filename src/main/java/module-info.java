module ru.sinitsynme.socketfilword {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens ru.sinitsynme.socketfilword to javafx.fxml;
    exports ru.sinitsynme.socketfilword;
    exports ru.sinitsynme.socketfilword.controller;
    opens ru.sinitsynme.socketfilword.controller to javafx.fxml;
}