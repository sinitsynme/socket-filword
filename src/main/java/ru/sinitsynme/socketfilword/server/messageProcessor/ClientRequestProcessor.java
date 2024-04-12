package ru.sinitsynme.socketfilword.server.messageProcessor;

import ru.sinitsynme.socketfilword.server.dto.AuthenticationRequestDto;
import ru.sinitsynme.socketfilword.server.dto.LevelRequestDto;
import ru.sinitsynme.socketfilword.server.dto.RegistrationRequestDto;
import ru.sinitsynme.socketfilword.service.AuthorizationService;
import ru.sinitsynme.socketfilword.service.LevelService;

import java.net.URISyntaxException;

public class ClientRequestProcessor {

    private final AuthorizationService authorizationService;
    private final LevelService levelService;

    public ClientRequestProcessor(AuthorizationService authorizationService, LevelService levelService) {
        this.authorizationService = authorizationService;
        this.levelService = levelService;
    }

    public Object processRequest(Object request) throws URISyntaxException {;
        if (request instanceof AuthenticationRequestDto authenticationRequestDto) {
            return processAuthenticationMessage(authenticationRequestDto);
        }
        else if (request instanceof RegistrationRequestDto registrationRequestDto) {
            return processRegistrationMessage(registrationRequestDto);
        }
        else if (request instanceof LevelRequestDto levelRequestDto) {
            return processLevelRequestMessage(levelRequestDto);
        }
        return new Object();
    }

    private Object processAuthenticationMessage(AuthenticationRequestDto requestDto) {
        return authorizationService.authorizeUser(requestDto);
    }
    private Object processRegistrationMessage(RegistrationRequestDto requestDto){
        return authorizationService.registerUser(requestDto);
    }

    private Object processLevelRequestMessage(LevelRequestDto levelRequestDto) throws URISyntaxException {
        return levelService.getUnfinishedLevelContentForUser(levelRequestDto);
    }
}
