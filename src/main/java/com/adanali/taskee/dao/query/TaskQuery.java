package com.adanali.taskee.dao.query;

public enum TaskQuery {
    INSERT("INSERT INTO tasks (user_id, title, description, status) VALUES (?, ?, ?, ?)"),

    UPDATE("UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?"),

    DELETE("DELETE FROM tasks WHERE id = ?"),

    FIND_BY_ID("SELECT * FROM tasks WHERE id = ?"),

    FIND_ALL_BY_USER("SELECT * FROM tasks WHERE user_id = ? ORDER BY created_at DESC"),

    FIND_BY_USER_AND_STATUS("SELECT * FROM tasks WHERE user_id = ? AND status = ? ORDER BY created_at DESC"),

    COUNT_BY_USER("SELECT COUNT(*) FROM tasks WHERE user_id = ?");

    private final String query;

    TaskQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
