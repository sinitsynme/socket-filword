package ru.sinitsynme.socketfilword.server;

import ru.sinitsynme.socketfilword.server.dto.FinishConnectionDto;
import ru.sinitsynme.socketfilword.server.messageProcessor.ClientRequestProcessor;
import ru.sinitsynme.socketfilword.service.AuthorizationService;
import ru.sinitsynme.socketfilword.service.LevelService;
import ru.sinitsynme.socketfilword.service.StatisticsService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

import static ru.sinitsynme.socketfilword.repository.FilwordRepositories.STATISTICS_JDBC_REPOSITORY;
import static ru.sinitsynme.socketfilword.repository.FilwordRepositories.USER_JDBC_REPOSITORY;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private final ClientRequestProcessor clientRequestProcessor;

    public ServerThread(final Socket clientSocket) {
        this.clientSocket = clientSocket;
        clientRequestProcessor = new ClientRequestProcessor(
                new AuthorizationService(USER_JDBC_REPOSITORY),
                new LevelService(STATISTICS_JDBC_REPOSITORY),
                new StatisticsService(USER_JDBC_REPOSITORY, STATISTICS_JDBC_REPOSITORY));
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            while (true) {
                Object clientRequest = clientInputStream.readObject();

                if (clientRequest instanceof FinishConnectionDto) {
                    System.out.printf(">>> Успешно завершено соединение с клиентом %s\n", clientSocket.getRemoteSocketAddress());
                    clientOutputStream.writeObject(new FinishConnectionDto());
                    break;
                }

                Object response = clientRequestProcessor.processRequest(clientRequest);
                clientOutputStream.writeObject(response);
            }

        } catch (IOException e) {
            System.out.println(">>> Ошибка ввода-вывода! Соединение разорвано: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(">>> Ошибка нахождения класса! Соединение разорвано. " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
