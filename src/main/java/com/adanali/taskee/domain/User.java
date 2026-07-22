package com.adanali.taskee.domain;

import com.adanali.taskee.domain.enums.Role;

import java.time.LocalDateTime;

public class User{
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private LocalDateTime createdAt;
    private Role role;
    private boolean isActive;

    public User(Long id, String email, String password, String fullName, LocalDateTime createdAt, Role role, boolean isActive) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.role = role;
        this.isActive = isActive;
    }

    public User(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = Role.MEMBER;
        this.isActive = true;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", createdAt=" + createdAt +
                ", role=" + role +
                ", isActive" + isActive +
                '}';
    }
}
