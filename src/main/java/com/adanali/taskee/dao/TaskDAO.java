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

    List<Task> findAllByAssigneeId(Long assigneeId, int limit, int offset) throws SQLException;

    List<Task> findAllByAssigneeIdAndStatus(Long assigneeId, TaskStatus status, int limit, int offset) throws SQLException;

    List<Task> findDelegatedByOwner(long ownerId, int limit, int offset) throws SQLException;

    List<Task> findDelegatedByOwnerAndStatus(long ownerId, TaskStatus status, int limit, int offset) throws SQLException;

    // UPDATE
    void update(Task task) throws SQLException;

    // DELETE
    void deleteById(Long id) throws SQLException;

    // Utility Method: For Dashboard Stats
    long countByAssigneeId(Long assigneeId) throws SQLException;

    long countByAssigneeIdAndStatus(Long assigneeId, TaskStatus status) throws SQLException;

    long countDelegatedByOwner(long ownerId) throws SQLException;

    long countDelegatedByOwnerAndStatus(long ownerId, TaskStatus status) throws SQLException;

    long countAllGlobalTasks() throws SQLException;

    long countCompletedGlobalTasks() throws SQLException;
}
