package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record AuthenticationRequestDto(String username, String password) implements Serializable { }
