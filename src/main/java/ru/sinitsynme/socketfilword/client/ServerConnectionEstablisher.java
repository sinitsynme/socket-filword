package ru.sinitsynme.socketfilword.client;

import ru.sinitsynme.socketfilword.exception.SocketException;

import java.io.IOException;

public class ServerConnectionEstablisher {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8100;

    public static ServerConnection establishConnection() {
        try {
            return new ServerConnection(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            throw new SocketException("Не удалось установить соединение с сервером");
        }
    }
}
