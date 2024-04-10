package ru.sinitsynme.socketfilword.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.sinitsynme.socketfilword.FilwordApplication;
import ru.sinitsynme.socketfilword.server.dto.AuthenticationRequestDto;
import ru.sinitsynme.socketfilword.server.dto.AuthenticationResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static ru.sinitsynme.socketfilword.FilwordApplication.*;

public class AuthorizationController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label actionTarget;

    @FXML
    protected void signInButtonPressed() {
        actionTarget.setText("");
        String username = usernameField.getText();
        String password = passwordField.getText();
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(username, password);
        try {
            AuthenticationResponseDto responseDto = (AuthenticationResponseDto) serverConnection.doRequest(authenticationRequestDto);
            if (responseDto.status() == 0) {
                authorizationDto = responseDto.authorizationDto();
                routeTo("level.fxml");
            }
            else {
                actionTarget.setText(responseDto.message());
            }
        }  catch (IOException|ClassNotFoundException e) {
            System.out.println(e.getMessage());
            actionTarget.setText("Проблемы с подключением");
        }
    }

    @FXML
    protected void registrationButtonPressed() throws IOException {
        routeTo("register.fxml");
    }

    @FXML
    protected void authCall() throws IOException {
        System.out.println(authorizationDto);
    }


    private void routeTo(String route) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stageController.openScene(stage, route);
    }
}