package com.adanali.taskee.dao;

import com.adanali.taskee.config.DBConnectionManager;
import com.adanali.taskee.dao.query.UserQuery;
import com.adanali.taskee.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoJDBCImpl implements UserDAO{
    @Override
    public User save(User user) throws SQLException {
        try(Connection connection = DBConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.INSERT.getQuery(), Statement.RETURN_GENERATED_KEYS)){

            // INSERT INTO users (email, password, full_name) VALUES (?, ?, ?)
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Failed to Save the User, No rows affected.");

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    return findById(generatedKeys.getLong(1)).orElseThrow(
                            () -> new SQLException("Failed to Save the User, Saved User not found.")
                    );
                }else throw new SQLException("Failed to Save the User, No ID obtained.");
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.FIND_BY_ID.getQuery())) {

            // SELECT * FROM users WHERE id = ?
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.FIND_BY_EMAIL.getQuery())) {

            // SELECT * FROM users WHERE email = ?
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException{
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.FIND_ALL.getQuery());
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        }
        return users;
    }

    @Override
    public void update(User user) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.UPDATE.getQuery())) {

            // UPDATE users SET full_name = ?, password = ? WHERE id = ?
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Failed to Update the User, No rows affected.");
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.DELETE.getQuery())) {

            // DELETE FROM users WHERE id = ?
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Failed to Delete the User, No rows affected.");
        }
    }

    @Override
    public boolean exists(String email) throws SQLException{
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.EXISTS.getQuery())) {

            // SELECT 1 FROM users WHERE email = ?
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("full_name"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
