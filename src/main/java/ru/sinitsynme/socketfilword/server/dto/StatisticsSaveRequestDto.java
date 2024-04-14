package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record StatisticsSaveRequestDto(int levelId, int completionTimeInSeconds, AuthorizationDto authorizationDto) implements Serializable {
}
