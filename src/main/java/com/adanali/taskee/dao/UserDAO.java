package com.adanali.taskee.dao;

import com.adanali.taskee.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    // CREATE
    User save(User user) throws SQLException;

    // READ
    Optional<User> findById(Long id) throws SQLException ;
    Optional<User> findByEmail(String email) throws SQLException ;
    List<User> findAll() throws SQLException ;

    // Update
    void update(User user) throws SQLException ;

    // Delete
    void deleteById(Long id) throws SQLException ;

    // Utility Method
    boolean exists(String email) throws SQLException ;
}
