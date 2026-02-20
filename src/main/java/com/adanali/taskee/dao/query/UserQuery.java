package com.adanali.taskee.dao.query;

public enum UserQuery {
    INSERT("INSERT INTO users (email, password, full_name) VALUES (?, ?, ?)"),

    FIND_BY_ID("SELECT * FROM users WHERE id = ?"),

    FIND_BY_EMAIL("SELECT * FROM users WHERE email = ?"),

    FIND_ALL("SELECT * FROM users"),

    UPDATE("UPDATE users SET full_name = ?, password = ? WHERE id = ?"),

    DELETE("DELETE FROM users WHERE id = ?"),

    EXISTS("SELECT 1 FROM users WHERE email = ?");

    private final String query;

    UserQuery(String query){
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
