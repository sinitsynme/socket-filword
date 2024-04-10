package ru.sinitsynme.socketfilword.repository;

public class FilwordRepositories {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/filword";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public static final UserJdbcRepository USER_JDBC_REPOSITORY;
    public static final StatisticsJdbcRepository STATISTICS_JDBC_REPOSITORY;

    static {
        USER_JDBC_REPOSITORY = new UserJdbcRepository(DB_URL, DB_USERNAME, DB_PASSWORD);
        STATISTICS_JDBC_REPOSITORY = new StatisticsJdbcRepository(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
