package ru.sinitsynme.socketfilword;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ru.sinitsynme.socketfilword.client.ServerConnection;
import ru.sinitsynme.socketfilword.client.ServerConnectionEstablisher;
import ru.sinitsynme.socketfilword.controller.StageController;
import ru.sinitsynme.socketfilword.exception.SocketException;
import ru.sinitsynme.socketfilword.server.dto.AuthorizationDto;

import java.io.IOException;

public class FilwordApplication extends Application {
    private static final String APPLICATION_NAME = "Страна Филвордов. Синицын М.Е. 6413";
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    public static StageController stageController = new StageController();
    public static ServerConnection serverConnection;
    public static AuthorizationDto authorizationDto;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle(APPLICATION_NAME);
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> {
            authorizationDto = null;
            try {
                serverConnection.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            serverConnection = ServerConnectionEstablisher.establishConnection();
            FXMLLoader fxmlLoader = new FXMLLoader(FilwordApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
            stage.setScene(scene);
            stage.show();

        } catch (SocketException ex) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Не удалось подключиться к серверу");
            alert.setTitle(APPLICATION_NAME);
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            start(stage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

    }

    public static void main(String[] args) {
        launch();
    }
}