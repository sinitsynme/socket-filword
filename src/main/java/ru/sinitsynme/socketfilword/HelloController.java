package ru.sinitsynme.socketfilword;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

import static ru.sinitsynme.socketfilword.FilwordApplication.serverConnection;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException, ClassNotFoundException {
        welcomeText.setText("Welcome to JavaFX Application!");
        serverConnection.close();
    }
}