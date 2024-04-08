package ru.sinitsynme.socketfilword.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {

    private static final int PORT = 8100;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(">>> Сервер FILWORD_LAND запущен ");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf(">>> Подключен клиент %s\n", clientSocket.getRemoteSocketAddress());
                Thread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }
        } catch (IOException e) {
            System.out.println(">>> Ошибка соединения. Работа сервера завершена ");
        }
    }

}
