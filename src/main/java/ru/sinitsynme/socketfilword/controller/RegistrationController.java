package ru.sinitsynme.socketfilword.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.sinitsynme.socketfilword.server.dto.RegistrationRequestDto;
import ru.sinitsynme.socketfilword.server.dto.RegistrationResponseDto;

import java.io.IOException;

import static ru.sinitsynme.socketfilword.FilwordApplication.serverConnection;
import static ru.sinitsynme.socketfilword.FilwordApplication.stageController;

public class RegistrationController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label actionTarget;

    @FXML
    protected void registrationButtonPressed() {
        actionTarget.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            actionTarget.setText("Пароли не совпадают!");
        } else {
            RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(username, password);
            try {
                RegistrationResponseDto registrationResponseDto = (RegistrationResponseDto) serverConnection.doRequest(registrationRequestDto);
                if (registrationResponseDto.status() == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Вы успешно зарегистрировались!");
                    alert.setTitle("Успешная регистрация");
                    alert.showAndWait()
                            .filter(response -> response == ButtonType.OK)
                            .ifPresent(response -> {
                                try {
                                    routeTo("login.fxml");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
                else {
                    actionTarget.setText(registrationResponseDto.message());
                }
            } catch (IOException | ClassNotFoundException e) {
                actionTarget.setText("Проблемы с подключением");
            }
        }
    }

    @FXML
    protected void signinButtonPressed() throws IOException {
        routeTo("login.fxml");
    }

    private void routeTo(String route) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stageController.openScene(stage, route);
    }

}
