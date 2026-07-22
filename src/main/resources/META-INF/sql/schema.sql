-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS taskee;
USE taskee;

-- 2. Create Users Table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       role ENUM('ADMIN', 'MEMBER') DEFAULT 'MEMBER',
                       is_active BOOLEAN DEFAULT TRUE,
                       createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Create Tasks Table
CREATE TABLE tasks (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       owner_id BIGINT NOT NULL,
                       assignee_id BIGINT DEFAULT NULL,
                       title VARCHAR(150) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) DEFAULT 'PENDING',
                       createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                       FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
                       FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 4. Best Practice: Creating Indexes
-- We will frequently search for tasks by owner and by assignee.
CREATE INDEX idx_tasks_owner ON tasks(owner_id);
CREATE INDEX idx_tasks_assignee ON tasks(assignee_id);

-- 5. Seed the reserved admin account expected by the System
-- email:    admin@taskee.com
-- password: Admin@123
--
-- Note: this BCrypt hash was prepared externally. If your local jBCrypt
-- setup rejects the prefix variant, regenerate the hash with jBCrypt
-- and keep the same INSERT.
INSERT INTO users (email, password, full_name, role, is_active)
VALUES (
           'admin@taskee.com',
           '$2y$10$3g.6q3PEqaWrzDbViJrAfeh794V.M19/..6hifxctKiJ3I0Ud08G.',
           'System Administrator',
           'ADMIN',
           TRUE
       ) ON DUPLICATE KEY UPDATE email = email;