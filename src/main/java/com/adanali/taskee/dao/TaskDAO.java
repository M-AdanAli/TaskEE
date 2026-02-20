package com.adanali.taskee.dao;

import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TaskDAO {

    // CREATE
    Task save(Task task) throws SQLException;

    // READ
    Optional<Task> findById(Long id) throws SQLException;

    List<Task> findAllByUserId(Long userId) throws SQLException;

    List<Task> findAllByUserIdAndStatus(Long userId, TaskStatus status) throws SQLException;

    // UPDATE
    void update(Task task) throws SQLException;

    // DELETE
    void deleteById(Long id) throws SQLException;

    // Utility Method: For Dashboard Stats
    int countByUserId(Long userId) throws SQLException;
}
