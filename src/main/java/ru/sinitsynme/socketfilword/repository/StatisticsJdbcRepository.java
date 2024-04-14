package ru.sinitsynme.socketfilword.repository;


import ru.sinitsynme.socketfilword.domain.Statistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatisticsJdbcRepository extends AbstractJdbcRepository<Statistics, UUID> {


    public StatisticsJdbcRepository(String dbUrl, String dbUsername, String dbPassword) {
        super(dbUrl, dbUsername, dbPassword);
    }

    public List<Statistics> getByUserId(Integer userId) {
        List<Statistics> statisticsList = new ArrayList<>();

        try {
            Connection connection = getConnection();
            String request = "SELECT * FROM statistics WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Statistics statistics = new Statistics();
                statistics.setId(UUID.fromString(resultSet.getString(1)));
                statistics.setUserId(resultSet.getInt(2));
                statistics.setLevelId(resultSet.getInt(3));
                statistics.setCompletionTimeInSeconds(resultSet.getInt(4));
                statisticsList.add(statistics);
            }

            return statisticsList;

        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    public List<Statistics> getByLevelId(Integer levelId) {
        List<Statistics> statisticsList = new ArrayList<>();

        try {
            Connection connection = getConnection();
            String request = "SELECT * FROM statistics WHERE level_id = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, levelId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Statistics statistics = new Statistics();
                statistics.setId(UUID.fromString(resultSet.getString(1)));
                statistics.setUserId(resultSet.getInt(2));
                statistics.setLevelId(resultSet.getInt(3));
                statistics.setCompletionTimeInSeconds(resultSet.getInt(4));
                statisticsList.add(statistics);
            }

            return statisticsList;

        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    @Override
    public List<Statistics> getAll() {
        List<Statistics> statisticsList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM statistics";
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                Statistics statistics = new Statistics();
                statistics.setId(UUID.fromString(resultSet.getString(1)));
                statistics.setUserId(resultSet.getInt(2));
                statistics.setLevelId(resultSet.getInt(3));
                statistics.setCompletionTimeInSeconds(resultSet.getInt(4));
                statisticsList.add(statistics);
            }
            closeStatement(statement);
            closeConnection(connection);
            return statisticsList;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    @Override
    public void add(Statistics statistics) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String request = String.format("INSERT INTO statistics(id, user_id, level_id, completion_time_seconds) VALUES ('%s', '%d', '%d', '%d')",
                    statistics.getId(),
                    statistics.getUserId(),
                    statistics.getLevelId(),
                    statistics.getCompletionTimeInSeconds());
            statement.executeUpdate(request);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            Connection connection = getConnection();
            String request = "DELETE FROM statistics WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }

    public void deleteByUserIdAndLevelId(int userId, int levelId) {
        try {
            Connection connection = getConnection();
            String request = "DELETE FROM statistics WHERE user_id = ? AND level_id = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, userId);
            statement.setInt(2, levelId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось выполнить запрос: %s", e.getMessage()));
        }
    }
}
