package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;
import java.util.List;

public record PlayerStatisticsResponseDto(int status, String message, List<SingleStatisticsResponseDto> statisticsList) implements Serializable {
}
