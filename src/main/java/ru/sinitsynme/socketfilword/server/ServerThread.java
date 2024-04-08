package ru.sinitsynme.socketfilword.server;

import ru.sinitsynme.socketfilword.server.dto.FinishConnectionDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket clientSocket;

    public ServerThread(final Socket clientSocket) {
        this.clientSocket = clientSocket;
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

                Object response = prepareResponse(clientRequest);
                clientOutputStream.writeObject(response);
            }

        } catch (IOException e) {
            System.out.println(">>> Ошибка ввода-вывода! Соединение разорвано: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(">>> Ошибка нахождения класса! Соединение разорвано. " + e.getMessage());
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Object prepareResponse(Object clientRequest) {
        return new Object();
    }
}
