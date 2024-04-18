package ru.sinitsynme.socketfilword.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

import static ru.sinitsynme.socketfilword.FilwordApplication.authorizationDto;
import static ru.sinitsynme.socketfilword.FilwordApplication.stageController;

public class StatsController {
    @FXML
    public HBox statsHBox;
    @FXML
    public Button logoutButton;
    @FXML
    public GridPane statsPane;

    @FXML
    protected void initialize() throws IOException, ClassNotFoundException {

    }

    @FXML
    protected void logout() throws IOException {
        authorizationDto = null;
        routeTo("login.fxml");
    }

    @FXML
    private void routeTo(String route) throws IOException {
        Stage stage = (Stage) statsHBox.getScene().getWindow();
        stageController.openScene(stage, route);
    }

}
