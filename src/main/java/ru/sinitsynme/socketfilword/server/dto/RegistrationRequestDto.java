package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record RegistrationRequestDto(String username, String password) implements Serializable {
}
