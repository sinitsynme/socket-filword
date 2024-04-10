package ru.sinitsynme.socketfilword.service;

import ru.sinitsynme.socketfilword.domain.FilwordUser;
import ru.sinitsynme.socketfilword.repository.UserJdbcRepository;
import ru.sinitsynme.socketfilword.server.dto.*;

import java.util.Optional;

public class AuthorizationService {
    private final UserJdbcRepository userJdbcRepository;

    public AuthorizationService(UserJdbcRepository userJdbcRepository) {
        this.userJdbcRepository = userJdbcRepository;
    }

    public RegistrationResponseDto registerUser(RegistrationRequestDto requestDto) {
        if(userJdbcRepository.existsByUsername(requestDto.username())) {
            return new RegistrationResponseDto(-1, "Пользователь с таким именем уже существует");
        }
        try {
            FilwordUser filwordUser = new FilwordUser(requestDto.username(), requestDto.password());
            userJdbcRepository.add(filwordUser);
            System.out.printf("Пользователь с именем %s зарегистрирован\n", requestDto.username());
            return new RegistrationResponseDto(0, "");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new RegistrationResponseDto(-2, "Что-то пошло не так, обратитесь к администратору");
        }
    }

    public AuthenticationResponseDto authorizeUser(AuthenticationRequestDto authenticationRequestDto) {
        try {
            Optional<FilwordUser> filwordUserOptional = userJdbcRepository.getUser(authenticationRequestDto.username());
            if (filwordUserOptional.isEmpty()) {
                return new AuthenticationResponseDto(-1, "Неправильный логин или пароль", null);
            }

            FilwordUser filwordUser = filwordUserOptional.get();
            if(!authenticationRequestDto.password().equals(filwordUser.getPassword())) {
                return new AuthenticationResponseDto(-2, "Неправильный логин или пароль", null);
            }

            return new AuthenticationResponseDto(0, "", new AuthorizationDto(filwordUser.getUsername(), filwordUser.getId()));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new AuthenticationResponseDto(-3, "Что-то пошло не так, обратитесь к администратору", null);
        }
    }
}