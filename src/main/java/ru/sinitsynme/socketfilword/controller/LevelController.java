package ru.sinitsynme.socketfilword.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.sinitsynme.socketfilword.domain.LevelLetter;
import ru.sinitsynme.socketfilword.server.dto.LevelRequestDto;
import ru.sinitsynme.socketfilword.server.dto.LevelResponseDto;

import java.io.IOException;
import java.util.List;

import static ru.sinitsynme.socketfilword.FilwordApplication.*;

public class LevelController {
    private static final int NANO = 1_000_000_000;
    @FXML
    private VBox container;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label timerLabel;
    private Timeline oneSecTick;
    private long startTimeSeconds;
    private long nowTimeSeconds;
    private boolean timeInitialized;
    private List<List<LevelLetter>> levelContent;

    @FXML
    protected void initialize() throws IOException, ClassNotFoundException {
        LevelResponseDto responseDto = (LevelResponseDto) serverConnection.doRequest(new LevelRequestDto(authorizationDto));

        if (responseDto.status() == 0) {

            levelContent = responseDto.levelContent();
            int levelSize = levelContent.size();

            for (int rowIndex = 0; rowIndex < levelSize; rowIndex++) {
                for (int colIndex = 0; colIndex < levelSize; colIndex++) {
                    Button button = new Button(String.valueOf(levelContent.get(rowIndex).get(colIndex).letter()));
                    button.setOnAction(e -> {
                        button.setStyle("-fx-font-size:28;-fx-text-fill:red");
                    });
                    button.setStyle("-fx-font-size:28");
                    GridPane.setFillWidth(button, true);
                    GridPane.setFillHeight(button, true);
                    gridPane.add(button, colIndex, rowIndex);
                }
            }

            gridPane.setHgap(1);
            gridPane.setVgap(1);

            oneSecTick = new Timeline(new KeyFrame(Duration.seconds(1), event -> setTime()));
            oneSecTick.setCycleCount(Timeline.INDEFINITE);
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!timeInitialized) {
                        startTimeSeconds = now / NANO;
                        timeInitialized = true;
                    }
                    nowTimeSeconds = now / NANO;
                    oneSecTick.play();
                }
            };
            timer.start();
        }

    }

    @FXML
    protected void logout() throws IOException {
        authorizationDto = null;
        routeTo("login.fxml");
    }

    private void setTime() {
        long time = nowTimeSeconds - startTimeSeconds;
        long mins = time / 60;
        long secs = time % 60;
        timerLabel.setText(String.format("%02d:%02d", mins, secs));
    }

    private void routeTo(String route) throws IOException {
        Stage stage = (Stage) timerLabel.getScene().getWindow();
        stageController.openScene(stage, route);
    }

}
