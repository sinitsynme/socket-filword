package ru.sinitsynme.socketfilword.server.messageProcessor;

import ru.sinitsynme.socketfilword.server.dto.*;
import ru.sinitsynme.socketfilword.service.AuthorizationService;
import ru.sinitsynme.socketfilword.service.LevelService;
import ru.sinitsynme.socketfilword.service.StatisticsService;

import java.net.URISyntaxException;

public class ClientRequestProcessor {

    private final AuthorizationService authorizationService;
    private final LevelService levelService;
    private final StatisticsService statisticsService;

    public ClientRequestProcessor(AuthorizationService authorizationService, LevelService levelService, StatisticsService statisticsService) {
        this.authorizationService = authorizationService;
        this.levelService = levelService;
        this.statisticsService = statisticsService;
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
        else if (request instanceof StatisticsSaveRequestDto statisticsSaveRequestDto) {
            return processSaveStatisticsMessage(statisticsSaveRequestDto);
        }
        else if (request instanceof StatisticsTopRequestDto statisticsTopRequestDto) {
            return processStatisticsTopMessage(statisticsTopRequestDto);
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

    private Object processSaveStatisticsMessage(StatisticsSaveRequestDto statisticsSaveRequestDto) {
        return statisticsService.saveStatistics(statisticsSaveRequestDto);
    }

    private Object processStatisticsTopMessage(StatisticsTopRequestDto statisticsTopRequestDto) {
        return statisticsService.getTopStatisticsWithCurrentPlayer(statisticsTopRequestDto);
    }
}
