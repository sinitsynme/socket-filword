package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record StatisticsSaveResponseDto(int status, String message) implements Serializable {
}
