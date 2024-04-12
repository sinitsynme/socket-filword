package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record LevelRequestDto(AuthorizationDto authorizationDto) implements Serializable {
}
