package ru.sinitsynme.socketfilword.repository;

import ru.sinitsynme.socketfilword.domain.FilwordUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserJdbcRepository extends AbstractJdbcRepository<FilwordUser, Integer> {


    public UserJdbcRepository(String dbUrl, String dbUsername, String dbPassword) {
        super(dbUrl, dbUsername, dbPassword);
    }

    public boolean existsByUsername(String username) {
        try {
            Connection connection = getConnection();
            String request = "SELECT * FROM filword_user WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    public Optional<FilwordUser> getUser(String username) {
        try {
            Connection connection = getConnection();
            String request = "SELECT * FROM filword_user WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return Optional.empty();

            FilwordUser filwordUser = new FilwordUser();
            filwordUser.setId(resultSet.getInt(1));
            filwordUser.setUsername(resultSet.getString(2));
            filwordUser.setPassword(resultSet.getString(3));

            return Optional.of(filwordUser);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    @Override
    public List<FilwordUser> getAll() {
        List<FilwordUser> users = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM filword_user";
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                FilwordUser filwordUser = new FilwordUser();
                filwordUser.setId(resultSet.getInt(1));
                filwordUser.setUsername(resultSet.getString(2));
                filwordUser.setPassword(resultSet.getString(3));
                users.add(filwordUser);
            }
            closeStatement(statement);
            closeConnection(connection);
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    @Override
    public void add(FilwordUser filwordUser) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String request = String.format("INSERT INTO filword_user(username, password) VALUES ('%s', '%s')", filwordUser.getUsername(), filwordUser.getPassword());
            statement.executeUpdate(request);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            Connection connection = getConnection();
            String request = "DELETE FROM filword_user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }
}
