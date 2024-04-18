package ru.sinitsynme.socketfilword.service;

import ru.sinitsynme.socketfilword.domain.FilwordUser;
import ru.sinitsynme.socketfilword.domain.Statistics;
import ru.sinitsynme.socketfilword.repository.StatisticsJdbcRepository;
import ru.sinitsynme.socketfilword.repository.UserJdbcRepository;
import ru.sinitsynme.socketfilword.server.dto.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class StatisticsService {
    private final UserJdbcRepository userJdbcRepository;
    private final StatisticsJdbcRepository statisticsJdbcRepository;

    public StatisticsService(UserJdbcRepository userJdbcRepository, StatisticsJdbcRepository statisticsJdbcRepository) {
        this.userJdbcRepository = userJdbcRepository;
        this.statisticsJdbcRepository = statisticsJdbcRepository;
    }

    public StatisticsSaveResponseDto saveStatistics(StatisticsSaveRequestDto requestDto) {
        if (requestDto.authorizationDto() == null) {
            return new StatisticsSaveResponseDto(-1, "Пользователь не авторизован!");
        }
        Statistics statistics = new Statistics();
        statistics.setUserId(requestDto.authorizationDto().id());
        statistics.setLevelId(requestDto.levelId());
        statistics.setCompletionTimeInSeconds(requestDto.completionTimeInSeconds());
        statistics.setId(UUID.randomUUID());
        try {
            statisticsJdbcRepository.deleteByUserIdAndLevelId(requestDto.authorizationDto().id(), requestDto.levelId());
            statisticsJdbcRepository.add(statistics);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return new StatisticsSaveResponseDto(-2, "Что-то пошло не так, обратитесь к администратору!");
        }
        return new StatisticsSaveResponseDto(0, "");
    }

    public PlayerStatisticsResponseDto getPlayerStatistics(PlayerStatisticsRequestDto requestDto) {
        if (requestDto.authorizationDto() == null) {
            return new PlayerStatisticsResponseDto(-1, "Пользователь не авторизован!", null);
        }
        FilwordUser filwordUser = userJdbcRepository.getUserById(requestDto.authorizationDto().id()).orElseThrow();
        List<SingleStatisticsResponseDto> statisticsList = statisticsJdbcRepository
                .getByUserId(requestDto.authorizationDto().id()).stream()
                .map(stat -> new SingleStatisticsResponseDto(stat.getLevelId(), filwordUser.getUsername(), stat.getCompletionTimeInSeconds()))
                .sorted(Comparator.comparingInt(SingleStatisticsResponseDto::levelId))
                .toList();
        return new PlayerStatisticsResponseDto(0, "", statisticsList);
    }

    public StatisticsTopResponseDto getTopStatisticsWithCurrentPlayer(StatisticsTopRequestDto requestDto) {
        List<Statistics> statisticsList = statisticsJdbcRepository.getByLevelId(requestDto.levelId());
        statisticsList.sort(Comparator.comparingInt(Statistics::getCompletionTimeInSeconds));
        List<StatisticsResponseDto> statisticsResponseDtos = new ArrayList<>();

        try {
            for (int i = 0; i < 5 && i < statisticsList.size(); i++) {
                Statistics statistics = statisticsList.get(i);
                FilwordUser filwordUser = userJdbcRepository.getUserById(statistics.getUserId())
                        .orElseThrow(() -> new RuntimeException("Статистика нарушена!"));
                StatisticsResponseDto responseDto = new StatisticsResponseDto(
                        i + 1,
                        statistics.getCompletionTimeInSeconds(),
                        filwordUser.getUsername()
                );
                statisticsResponseDtos.add(responseDto);
            }

            if (statisticsResponseDtos.stream().noneMatch(it -> it.username().equals(requestDto.authorizationDto().username()))) {
                for (int j = 5; j < statisticsList.size(); j++) {
                    Statistics statistics = statisticsList.get(j);
                    if (statistics.getUserId() == requestDto.authorizationDto().id()) {
                        FilwordUser filwordUser = userJdbcRepository.getUserById(statistics.getUserId())
                                .orElseThrow(() -> new RuntimeException("Статистика нарушена!"));
                        StatisticsResponseDto responseDto = new StatisticsResponseDto(
                                j + 1,
                                statistics.getCompletionTimeInSeconds(),
                                filwordUser.getUsername()
                        );
                        statisticsResponseDtos.add(responseDto);
                    }
                }
            }
            return new StatisticsTopResponseDto(0, "", statisticsResponseDtos);

        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return new StatisticsTopResponseDto(-1, "Что-то пошло не так при подгрузке статистики, обратитесь к администратору", null);
        }
    }


}
