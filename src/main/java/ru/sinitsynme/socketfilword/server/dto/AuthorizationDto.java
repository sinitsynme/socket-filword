package ru.sinitsynme.socketfilword.server.dto;

import java.io.Serializable;

public record AuthorizationDto(String username, int id) implements Serializable { }
