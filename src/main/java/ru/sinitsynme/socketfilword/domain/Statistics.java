package ru.sinitsynme.socketfilword.domain;

import java.util.UUID;

public class Statistics {
    private UUID id;
    private int userId;
    private int levelId;
    private int completionTimeInSeconds;

    public Statistics() {
    }

    public Statistics(int userId, int levelId, int completionTimeInSeconds) {
        this.userId = userId;
        this.levelId = levelId;
        this.completionTimeInSeconds = completionTimeInSeconds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getCompletionTimeInSeconds() {
        return completionTimeInSeconds;
    }

    public void setCompletionTimeInSeconds(int completionTimeInSeconds) {
        this.completionTimeInSeconds = completionTimeInSeconds;
    }
}
