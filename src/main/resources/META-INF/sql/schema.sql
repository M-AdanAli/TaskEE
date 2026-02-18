-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS tasknexus;
USE tasknexus;

-- 2. Create Users Table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE, -- Unique constraint is critical
                       password VARCHAR(255) NOT NULL,     -- Big enough for BCrypt hash
                       full_name VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Create Tasks Table
CREATE TABLE tasks (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id BIGINT NOT NULL,            -- The Foreign Key
                       title VARCHAR(150) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) DEFAULT 'PENDING',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- constraint to ensure data integrity
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Best Practice: Creating an Index
-- We will frequently search for tasks belonging to a specific user.
-- Without an index, the DB has to scan the whole table.
CREATE INDEX idx_tasks_user ON tasks(user_id);