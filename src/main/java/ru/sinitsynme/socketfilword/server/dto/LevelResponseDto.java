package ru.sinitsynme.socketfilword.server.dto;

import ru.sinitsynme.socketfilword.domain.LevelLetter;

import java.io.Serializable;
import java.util.List;

public record LevelResponseDto(int status, String message,
                               int levelId,
                               List<List<LevelLetter>> levelContent) implements Serializable {
}
