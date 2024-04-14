package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record StatisticsResponseDto(int place, int completionTimeInSeconds, String username) implements Serializable {
}
