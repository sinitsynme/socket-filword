package ru.sinitsynme.socketfilword.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.sinitsynme.socketfilword.FilwordApplication;

import java.io.IOException;

public class StageController {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public void openScene(Stage stage, String resourceUrl) throws IOException {
        Parent root = FXMLLoader.load(FilwordApplication.class.getResource(resourceUrl));
        stage.setScene(new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT));
    }
}
