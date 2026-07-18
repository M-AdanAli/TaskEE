package com.adanali.taskee.dao.query;

public enum TaskQuery {
    INSERT("INSERT INTO tasks (owner_id, assignee_id, title, description, status) VALUES (?, ?, ?, ?, ?)"),

    UPDATE("UPDATE tasks SET title = ?, description = ?, status = ?, assignee_id=? WHERE id = ?"),

    DELETE("DELETE FROM tasks WHERE id = ?"),

    FIND_BY_ID("SELECT * FROM tasks WHERE id = ?"),

    FIND_ALL_BY_ASSIGNEE("SELECT * FROM tasks WHERE assignee_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?"),

    FIND_BY_ASSIGNEE_AND_STATUS("SELECT * FROM tasks WHERE assignee_id = ? AND status = ? ORDER BY created_at DESC LIMIT ? OFFSET ?"),

    COUNT_BY_ASSIGNEE("SELECT COUNT(*) FROM tasks WHERE assignee_id = ?"),

    COUNT_BY_ASSIGNEE_AND_STATUS("SELECT COUNT(*) FROM tasks WHERE assignee_id = ? AND status = ?"),

    FIND_DELEGATED_BY_OWNER("SELECT * FROM tasks WHERE owner_id = ? AND (assignee_id != ? OR assignee_id IS NULL) ORDER BY created_at DESC LIMIT ? OFFSET ?"),

    COUNT_DELEGATED_BY_OWNER("SELECT COUNT(*) FROM tasks WHERE owner_id = ? AND (assignee_id != ? OR assignee_id IS NULL)"),

    FIND_DELEGATED_BY_OWNER_AND_STATUS("SELECT * FROM tasks WHERE owner_id = ? AND (assignee_id != ? OR assignee_id IS NULL) AND status = ? ORDER BY created_at DESC LIMIT ? OFFSET ?"),

    COUNT_DELEGATED_BY_OWNER_AND_STATUS("SELECT COUNT(*) FROM tasks WHERE owner_id = ? AND (assignee_id != ? OR assignee_id IS NULL) AND status = ?"),

    COUNT_ALL_GLOBALLY("SELECT COUNT(*) FROM tasks"),

    COUNT_COMPLETED_GLOBALLY("SELECT COUNT(*) FROM tasks WHERE status='COMPLETED'");

    private final String query;

    TaskQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
