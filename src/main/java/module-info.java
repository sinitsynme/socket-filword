module ru.sinitsynme.socketfilword {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens ru.sinitsynme.socketfilword to javafx.fxml;
    exports ru.sinitsynme.socketfilword;
}