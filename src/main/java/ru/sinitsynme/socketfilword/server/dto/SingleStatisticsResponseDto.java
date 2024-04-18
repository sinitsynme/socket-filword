package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record SingleStatisticsResponseDto(int levelId, String username, int completionTimeInSeconds) implements Serializable {
}
