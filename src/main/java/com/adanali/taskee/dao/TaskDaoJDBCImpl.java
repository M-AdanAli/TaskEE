package com.adanali.taskee.dao;

import com.adanali.taskee.config.DBConnectionManager;
import com.adanali.taskee.dao.query.TaskQuery;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDaoJDBCImpl implements TaskDAO{

    @Override
    public Task save(Task task) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.INSERT.getQuery(), Statement.RETURN_GENERATED_KEYS)) {

            // INSERT INTO tasks (user_id, title, description, status) VALUES (?, ?, ?, ?)
            preparedStatement.setLong(1, task.getUserId());
            preparedStatement.setString(2, task.getTitle());
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getTaskStatus().name());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Failed to Create the Task, No rows affected.");

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return findById(generatedKeys.getLong(1)).orElseThrow(
                            ()-> new SQLException("Failed to Create the Task, Saved Task not found.")
                    );
                } else {
                    throw new SQLException("Failed to Create the Task, No ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(Task task) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.UPDATE.getQuery())) {

            // UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getTaskStatus().name());
            preparedStatement.setLong(4, task.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Failed to Update the Task, No rows affected.");
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.DELETE.getQuery())) {

            // DELETE FROM tasks WHERE id = ?
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Failed to Delete the Task, No rows affected.");
        }
    }

    @Override
    public Optional<Task> findById(Long id) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.FIND_BY_ID.getQuery())) {

            // SELECT * FROM tasks WHERE id = ?
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return Optional.of(mapResultSetToTask(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long userId) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.FIND_ALL_BY_USER.getQuery())) {

            // SELECT * FROM tasks WHERE user_id = ? ORDER BY created_at DESC
            preparedStatement.setLong(1, userId);

            return executeQuery(preparedStatement);
        }
    }

    @Override
    public List<Task> findAllByUserIdAndStatus(Long userId, TaskStatus status) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.FIND_BY_USER_AND_STATUS.getQuery())) {

            // SELECT * FROM tasks WHERE user_id = ? AND status = ? ORDER BY created_at DESC
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, status.name());

            return executeQuery(preparedStatement);
        }
    }

    @Override
    public int countByUserId(Long userId) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TaskQuery.COUNT_BY_USER.getQuery())) {

            // SELECT COUNT(*) FROM tasks WHERE user_id = ?
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

    // Helper methods:

    private List<Task> executeQuery(PreparedStatement preparedStatement) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                tasks.add(mapResultSetToTask(resultSet));
            }
        }
        return tasks;
    }

    private Task mapResultSetToTask(ResultSet resultSet) throws SQLException {
        return new Task(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                TaskStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                resultSet.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}
