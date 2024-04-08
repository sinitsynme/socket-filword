package ru.sinitsynme.socketfilword.exception;

public class SocketException extends RuntimeException {

    public SocketException() {
    }

    public SocketException(String message) {
        super(message);
    }

    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
