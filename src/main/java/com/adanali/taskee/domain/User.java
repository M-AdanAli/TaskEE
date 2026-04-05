package com.adanali.taskee.domain;

import java.time.LocalDateTime;

public class User{
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private LocalDateTime created_at;

    public User(Long id, String email, String password, String fullName, LocalDateTime created_at) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.created_at = created_at;
    }

    public User(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
