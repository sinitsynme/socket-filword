package ru.sinitsynme.socketfilword.service;

import org.apache.commons.io.FileUtils;
import ru.sinitsynme.socketfilword.FilwordApplication;
import ru.sinitsynme.socketfilword.domain.LevelLetter;
import ru.sinitsynme.socketfilword.domain.Statistics;
import ru.sinitsynme.socketfilword.repository.StatisticsJdbcRepository;
import ru.sinitsynme.socketfilword.server.dto.LevelRequestDto;
import ru.sinitsynme.socketfilword.server.dto.LevelResponseDto;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LevelService {

    private static final String LEVEL_FILE_PATH_PATTERN = "levels/%d.fil";
    private static final int[] levelsList = {1, 2, 3};

    private final StatisticsJdbcRepository statisticsJdbcRepository;

    public LevelService(StatisticsJdbcRepository statisticsJdbcRepository) {
        this.statisticsJdbcRepository = statisticsJdbcRepository;
    }

    public List<Statistics> getAllFinishedLevels(int userId) {
        return statisticsJdbcRepository.getByUserId(userId);
    }

    public LevelResponseDto getUnfinishedLevelContentForUser(LevelRequestDto requestDto) throws URISyntaxException {
        if (requestDto.authorizationDto() == null) {
            return new LevelResponseDto(-1, "Пользователь не авторизован", null);
        }
        int userId = requestDto.authorizationDto().id();
        int unfinishedLevelId = getUnfinishedLevelByUser(userId);

        if (unfinishedLevelId == -1) {
            return new LevelResponseDto(-2, "Все уровни пройдены! Поздравляем!", null);
        }

        List<List<LevelLetter>> levelContent = null;
        try {
            levelContent = getLevelContent(unfinishedLevelId);
            levelContent.forEach(System.out::println);
            return new LevelResponseDto(0, "", levelContent);

        } catch (IOException e) {
            return new LevelResponseDto(-3, "Уровень не загружен, обратитесь к администратору", null);
        }
    }

    public int getUnfinishedLevelByUser(int userId) {
        List<Integer> finishedLevels = getAllFinishedLevels(userId).stream()
                .map(Statistics::getLevelId)
                .toList();
        List<Integer> unfinishedLevels = Arrays.stream(levelsList)
                .filter(it -> !finishedLevels.contains(it))
                .boxed().toList();

        if (unfinishedLevels.isEmpty()) {
            return -1;
        }

        Random random = new Random();
        return unfinishedLevels.get(random.nextInt(unfinishedLevels.size()));
    }

    public List<List<LevelLetter>> getLevelContent(int levelId) throws URISyntaxException, IOException {
        List<List<LevelLetter>> levelContent = new ArrayList<>();
        URL resource = FilwordApplication.class.getResource(String.format(LEVEL_FILE_PATH_PATTERN, levelId));
        File file = new File(resource.getFile().replace("%20", " "));
        String data = FileUtils.readFileToString(file, "UTF-8");
        List<String> lines = Arrays.stream(data.split("\r\n")).toList();

        lines.forEach(line -> {
                    List<LevelLetter> row = new ArrayList<>();
                    String[] lettersInfo = line.split(";");
                    for (String letterInfo : lettersInfo) {
                        String[] info = letterInfo.split("-");
                        char letter = info[0].charAt(0);
                        int index = Integer.parseInt(info[1]);
                        int wordNum = Integer.parseInt(info[2]);
                        LevelLetter levelLetter = new LevelLetter(letter, index, wordNum);
                        row.add(levelLetter);
                    }

                    levelContent.add(row);
                }
        );
        return levelContent;

    }
}
