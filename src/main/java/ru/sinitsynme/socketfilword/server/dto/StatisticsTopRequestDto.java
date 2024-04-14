package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record StatisticsTopRequestDto(int levelId, AuthorizationDto authorizationDto) implements Serializable {
}
