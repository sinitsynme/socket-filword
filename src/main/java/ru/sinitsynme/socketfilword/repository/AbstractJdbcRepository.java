package ru.sinitsynme.socketfilword.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class AbstractJdbcRepository<T, ID> {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public AbstractJdbcRepository(String dbUrl, String dbUsername, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Соединение не удалось установить");
        }
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Соединение не удалось закрыть");
        }
    }

    public void closeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Запрос не удалось потушить");
        }
    }

    public abstract List<T> getAll();
    public abstract void add(T object);
    public abstract void delete(ID id);
}
