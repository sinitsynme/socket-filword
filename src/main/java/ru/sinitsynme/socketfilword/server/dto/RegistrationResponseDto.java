package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record RegistrationResponseDto(int status, String message) implements Serializable {
}
