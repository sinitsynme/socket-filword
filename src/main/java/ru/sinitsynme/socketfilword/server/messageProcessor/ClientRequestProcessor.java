package ru.sinitsynme.socketfilword.server.messageProcessor;

import ru.sinitsynme.socketfilword.server.dto.AuthenticationRequestDto;
import ru.sinitsynme.socketfilword.server.dto.FinishConnectionDto;
import ru.sinitsynme.socketfilword.server.dto.RegistrationRequestDto;
import ru.sinitsynme.socketfilword.service.AuthorizationService;

public class ClientRequestProcessor {

    private final AuthorizationService authorizationService;

    public ClientRequestProcessor(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Object processRequest(Object request) {;
        if (request instanceof AuthenticationRequestDto authenticationRequestDto) {
            return processAuthenticationMessage(authenticationRequestDto);
        }
        else if (request instanceof RegistrationRequestDto registrationRequestDto) {
            return processRegistrationMessage(registrationRequestDto);
        }
        return new Object();
    }

    private Object processAuthenticationMessage(AuthenticationRequestDto requestDto) {
        return authorizationService.authorizeUser(requestDto);
    }
    private Object processRegistrationMessage(RegistrationRequestDto requestDto){
        return authorizationService.registerUser(requestDto);
    }
}
