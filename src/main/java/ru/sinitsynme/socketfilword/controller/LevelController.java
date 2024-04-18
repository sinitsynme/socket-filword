package ru.sinitsynme.socketfilword.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.sinitsynme.socketfilword.FilwordApplication;
import ru.sinitsynme.socketfilword.domain.LevelLetter;
import ru.sinitsynme.socketfilword.server.dto.*;

import java.io.IOException;
import java.util.*;

import static ru.sinitsynme.socketfilword.FilwordApplication.*;

public class LevelController {
    private static final int NANO = 1_000_000_000;
    @FXML
    private VBox container;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane wordsPane;
    @FXML
    private Label timerLabel;
    @FXML
    private VBox guessedWordsBox;
    @FXML
    private Button logoutButton;
    private Timeline oneSecTick;
    private long startTimeSeconds;
    private long nowTimeSeconds;
    private boolean timeInitialized;
    private List<List<LevelLetter>> levelContent;
    private int levelId;
    private Map<Integer, Integer> wordsLengths = new HashMap<>();
    private Map<Button, LevelLetter> buttonsToLetters = new HashMap<>();
    private int currentSymbolIndex;
    private int currentWordIndex;
    private boolean gamerMistaken;
    private List<Button> guessedLetterButtons = new ArrayList<>();
    private List<Button> mistakenLetterButtons = new ArrayList<>();
    private List<Integer> guessedWords = new ArrayList<>();

    @FXML
    protected void initialize() throws IOException, ClassNotFoundException {
        LevelResponseDto responseDto = (LevelResponseDto) serverConnection.doRequest(new LevelRequestDto(authorizationDto));

        if (responseDto.status() == 0) {

            initializeComponents();

            levelContent = responseDto.levelContent();
            levelId = responseDto.levelId();
            fillWords(levelContent);
            int levelSize = levelContent.size();

            for (int rowIndex = 0; rowIndex < levelSize; rowIndex++) {
                for (int colIndex = 0; colIndex < levelSize; colIndex++) {

                    var wordLetter = levelContent.get(rowIndex).get(colIndex);

                    Button button = new Button(String.valueOf(wordLetter.letter()).toUpperCase());
                    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    button.setOnAction(e -> {
                        try {
                            processButtonAction(e, button);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    button.setStyle("-fx-font-size:28");
                    GridPane.setFillWidth(button, true);
                    GridPane.setFillHeight(button, true);
                    gridPane.add(button, colIndex, rowIndex);

                    buttonsToLetters.put(button, wordLetter);
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
        } else if (responseDto.status() == -2) {
            timerLabel.setVisible(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ура! Вы прошли все уровни! Ожидайте обновлений");
            alert.setTitle("Все уровни пройдены!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                    });
            gridPane.getChildren().clear();

            PlayerStatisticsResponseDto playerStatistics = (PlayerStatisticsResponseDto) serverConnection.doRequest(new PlayerStatisticsRequestDto(authorizationDto));
            if (playerStatistics.status() != 0) {
                Alert nalert = new Alert(Alert.AlertType.ERROR, "Вы прошли все уровни, но статистика не загрузилась");
                nalert.setTitle("Все уровни пройдены!");
                nalert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> logoutButton.fire());
            }
            else {
                container.getChildren().clear();
                List<SingleStatisticsResponseDto> statisticsResponseDtos = playerStatistics.statisticsList();
                VBox box = new VBox(10);
                box.setAlignment(Pos.CENTER);
                Label label = new Label("Статистика прохождения уровней");
                label.setStyle("-fx-font-weight: bold");
                box.getChildren().add(label);

                for (int i = 0; i < statisticsResponseDtos.size(); i++) {
                    var stats = statisticsResponseDtos.get(i);
                    box.getChildren().add(new Label(String.format("Уровень %d -> %d минут %d секунд",
                            stats.levelId(),
                            stats.completionTimeInSeconds() / 60,
                            stats.completionTimeInSeconds() % 60
                    )));
                }
                logoutButton.setVisible(false);
                Button button = new Button("Выйти из игры");
                button.setStyle("-fx-margin: 0 0 50 0");
                button.setOnAction(ev -> {
                    try {
                        authorizationDto = null;
                        Stage stage = (Stage) button.getScene().getWindow();
                        stageController.openScene(stage, "login.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                box.getChildren().add(button);
                container.getChildren().add(box);
            }
        }

    }

    private void initializeComponents() {
        logoutButton.setOnAction(e -> {
            try {
                logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Label label = new Label();
        label.setText("Отгаданные слова: ");
        guessedWordsBox.getChildren().add(0, label);
    }

    private void processButtonAction(ActionEvent e, Button button) throws IOException, ClassNotFoundException {
        LevelLetter wordLetter = buttonsToLetters.get(button);

        int newSymbolIndex = wordLetter.index();
        int newWordIndex = wordLetter.wordNum();

        if (currentSymbolIndex == newSymbolIndex && currentWordIndex == newWordIndex) {
            return;
        }

        if (gamerMistaken) {
            for (Button b : mistakenLetterButtons) {
                b.setStyle("-fx-font-size:28;-fx-text-fill:black");
            }
            mistakenLetterButtons.clear();
            gamerMistaken = false;
        }

        if (currentWordIndex == 0 && newSymbolIndex == 1) {
            currentWordIndex = newWordIndex;
            currentSymbolIndex = newSymbolIndex;
            button.setStyle("-fx-font-size:28;-fx-text-fill:green");
            guessedLetterButtons.add(button);
        } else if (currentWordIndex == newWordIndex && currentSymbolIndex == newSymbolIndex - 1) {
            currentSymbolIndex = newSymbolIndex;
            button.setStyle("-fx-font-size:28;-fx-text-fill:green");
            guessedLetterButtons.add(button);

            if (currentSymbolIndex == wordsLengths.get(currentWordIndex) && guessedLetterButtons.size() == wordsLengths.get(currentWordIndex)) {
                for (var b : guessedLetterButtons) {
                    b.setVisible(false);
                }
                StringBuilder sb = new StringBuilder();
                guessedLetterButtons.forEach(it -> sb.append(it.getText()));
                wordsPane.add(new Label(sb.toString()), 0, guessedWords.size());
                guessedWords.add(currentWordIndex);

                if (guessedWords.size() == wordsLengths.size()) {
                    finishGame();
                }

                guessedLetterButtons.clear();
                currentWordIndex = 0;
                currentSymbolIndex = 0;
            }
        } else {

            for (var b : guessedLetterButtons) {
                mistakenLetterButtons.add(b);
                b.setStyle("-fx-font-size:28;-fx-text-fill:red");
            }
            if (newSymbolIndex == 1 && newWordIndex == currentWordIndex) {
                mistakenLetterButtons.remove(0);
            }

            currentWordIndex = 0;
            currentSymbolIndex = 0;
            gamerMistaken = true;
            guessedLetterButtons.clear();

            if (newSymbolIndex == 1) {
                currentWordIndex = newWordIndex;
                currentSymbolIndex = newSymbolIndex;
                button.setStyle("-fx-font-size:28;-fx-text-fill:green");
                guessedLetterButtons.add(button);
            } else {
                mistakenLetterButtons.add(button);
                button.setStyle("-fx-font-size:28;-fx-text-fill:red");
            }


        }
    }

    private void fillWords(List<List<LevelLetter>> levelContent) {
        for (int i = 0; i < levelContent.size(); i++) {
            for (int j = 0; j < levelContent.size(); j++) {
                var wordLetter = levelContent.get(i).get(j);

                if (!wordsLengths.containsKey(wordLetter.wordNum())) {
                    wordsLengths.put(wordLetter.wordNum(), wordLetter.index());
                } else if (wordsLengths.get(wordLetter.wordNum()) < wordLetter.index()) {
                    wordsLengths.replace(wordLetter.wordNum(), wordLetter.index());
                }
            }
        }
    }

    private void finishGame() throws IOException, ClassNotFoundException {
        timerLabel.setVisible(false);
        int completionTimeInSeconds = (int) (nowTimeSeconds - startTimeSeconds);

        StatisticsSaveRequestDto statisticsSaveRequestDto = new StatisticsSaveRequestDto(levelId, completionTimeInSeconds, authorizationDto);
        StatisticsSaveResponseDto statisticsSaveResponseDto = (StatisticsSaveResponseDto) serverConnection.doRequest(statisticsSaveRequestDto);

        if (statisticsSaveResponseDto.status() == 0) {
            StatisticsTopRequestDto statisticsTopRequestDto = new StatisticsTopRequestDto(levelId, authorizationDto);
            StatisticsTopResponseDto statisticsTopResponseDto = (StatisticsTopResponseDto) serverConnection.doRequest(statisticsTopRequestDto);

            if (statisticsTopResponseDto.status() == 0) {

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5 && i < statisticsTopResponseDto.statisticsList().size(); i++) {
                    StatisticsResponseDto st = statisticsTopResponseDto.statisticsList().get(i);
                    sb.append(String.format("%d. %s >>> %d м %d с \n",
                            st.place(),
                            st.username(),
                            st.completionTimeInSeconds() / 60,
                            st.completionTimeInSeconds() % 60)
                    );
                }

                if (statisticsTopResponseDto.statisticsList().size() == 6) {
                    StatisticsResponseDto playerSt = statisticsTopResponseDto.statisticsList().get(5);
                    sb.append("..........\n");
                    sb.append(String.format("%d. %s >>> %d м %d с \n",
                            playerSt.place(),
                            playerSt.username(),
                            playerSt.completionTimeInSeconds() / 60,
                            playerSt.completionTimeInSeconds() % 60)
                    );
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Уровень пройден!\nСтатистика по уровню:\n\n" + sb);
                alert.setTitle("Уровень пройден!");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> {
                            try {
                                routeTo("level.fxml");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Уровень пройден! Статистика по уровню недоступна");
                alert.setTitle("Уровень пройден!");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> {
                            try {
                                routeTo("level.fxml");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Сохранение результатов игры прошло неуспешно");
            alert.setTitle("Ошибка");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            routeTo("level.fxml");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
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

    @FXML
    private void routeTo(String route) throws IOException {
        Stage stage = (Stage) guessedWordsBox.getScene().getWindow();
        stageController.openScene(stage, route);
    }

}
