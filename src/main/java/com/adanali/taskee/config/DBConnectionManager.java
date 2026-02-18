package com.adanali.taskee.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionManager {
    private static final HikariDataSource dataSource;

    static {
        // IMPORTANT : Check out "src/main/resources/db.properties.example" File

        // Loading the external properties to get DB Credentials
        try(InputStream dbPropertiesInput = DBConnectionManager.class.getClassLoader().getResourceAsStream("db.properties")){

            if (dbPropertiesInput == null){
                throw new RuntimeException("Unable to find 'db.properties' in the 'resources' folder");
            }

            Properties dbProperties = new Properties();
            dbProperties.load(dbPropertiesInput);

            // Setting up the Configurations for Hikari Connection Pool
            HikariConfig dbConfig = new HikariConfig();
            dbConfig.setJdbcUrl(dbProperties.getProperty("db.url"));
            dbConfig.setUsername(dbProperties.getProperty("db.username"));
            dbConfig.setPassword(dbProperties.getProperty("db.password"));
            dbConfig.setDriverClassName(dbProperties.getProperty("db.driver"));

            dbConfig.setMaximumPoolSize(10);
            dbConfig.setMinimumIdle(2);
            dbConfig.setIdleTimeout(30000);
            dbConfig.setConnectionTimeout(20000);

            dataSource = new HikariDataSource(dbConfig);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error initializing HikariCP", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}