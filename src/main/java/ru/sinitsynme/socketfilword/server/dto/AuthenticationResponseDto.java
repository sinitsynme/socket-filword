package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record AuthenticationResponseDto(int status, String message, AuthorizationDto authorizationDto) implements Serializable {
}
