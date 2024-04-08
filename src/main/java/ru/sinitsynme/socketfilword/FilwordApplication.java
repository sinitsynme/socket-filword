package ru.sinitsynme.socketfilword;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.sinitsynme.socketfilword.client.ServerConnection;
import ru.sinitsynme.socketfilword.client.ServerConnectionEstablisher;

import java.io.IOException;

public class FilwordApplication extends Application {

    public static ServerConnection serverConnection;

    @Override
    public void start(Stage stage) throws IOException {
        serverConnection = ServerConnectionEstablisher.establishConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(FilwordApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}