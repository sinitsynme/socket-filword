package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record PlayerStatisticsRequestDto(AuthorizationDto authorizationDto) implements Serializable {
}
