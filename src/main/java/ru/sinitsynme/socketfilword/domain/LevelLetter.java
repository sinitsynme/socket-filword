package ru.sinitsynme.socketfilword.domain;

import java.io.Serializable;

public record LevelLetter (char letter, int index, int wordNum) implements Serializable {
}
