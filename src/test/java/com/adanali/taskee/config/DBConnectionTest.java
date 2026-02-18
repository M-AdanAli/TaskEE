package com.adanali.taskee.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {

    @Test
    @DisplayName("Database Connection Pool should be working")
    void test_database_connection(){
        try (Connection connection = DBConnectionManager.getConnection()) {
            System.out.println("Connection established: " + connection);

            Assertions.assertNotNull(connection, "Connection should not be null");
            Assertions.assertFalse(connection.isClosed(), "Connection should be open");

        } catch (SQLException e) {
            Assertions.fail("Connection failed: " + e.getMessage());
        }
    }
}
