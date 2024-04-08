package ru.sinitsynme.socketfilword.client;

import ru.sinitsynme.socketfilword.server.dto.FinishConnectionDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {

    private final Socket socket;
    private final ObjectOutputStream requestStream;
    private final ObjectInputStream resultStream;

    public ServerConnection(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        requestStream = new ObjectOutputStream(socket.getOutputStream());
        resultStream = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException, ClassNotFoundException {
        doRequest(new FinishConnectionDto());
        requestStream.close();
        resultStream.close();
        socket.close();
    }

    public Object doRequest(Object request) throws IOException, ClassNotFoundException {
        requestStream.writeObject(request);
        return resultStream.readObject();
    }
}
