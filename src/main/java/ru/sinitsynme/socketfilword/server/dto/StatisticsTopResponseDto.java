package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;
import java.util.List;

public record StatisticsTopResponseDto(int status, String message, List<StatisticsResponseDto> statisticsList) implements Serializable {
}
