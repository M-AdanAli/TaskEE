package com.adanali.taskee.dao;

import com.adanali.taskee.config.DBConnectionManager;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.User;
import com.adanali.taskee.domain.enums.TaskStatus;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskDAOTest {

    private static TaskDAO taskDAO;
    private static UserDAO userDAO;
    private static User testUser;

    @BeforeAll
    static void setup() {
        taskDAO = new TaskDaoJDBCImpl();
        userDAO = new UserDaoJDBCImpl();

        // Parent User that owns the tasks
        Assertions.assertDoesNotThrow(()->{
            User user = new User("task_tester@example.com", "password", "Task Tester");
            testUser = userDAO.save(user);
        });

    }

    @AfterAll
    static void tearDown() {
        // Cleanup: Delete the user (Cascade should delete tasks too)
        Assertions.assertDoesNotThrow(()->{
            if (testUser != null && testUser.getId() != null) {
                userDAO.deleteById(testUser.getId());
            }
        });
    }

    // Optional: Clean tasks between tests if needed
    @BeforeEach
    void cleanTasks() {
        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM tasks WHERE user_id = " + testUser.getId());
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Test
    @Order(1)
    void testSaveTask() {
        Assertions.assertDoesNotThrow(()->{
            Task task = new Task( "Learn JUnit", "Study TDD", testUser.getId());
            Task savedTask = taskDAO.save(task);
            Assertions.assertNotNull(savedTask.getId());
            Assertions.assertNotNull(savedTask.getCreatedAt(), "Timestamp should be populated by the DB refresh");
            Assertions.assertEquals("Learn JUnit", savedTask.getTitle());
        });
    }

    @Test
    @Order(2)
    void testFindAllByUserId() {
        // Adding more tasks
        Assertions.assertDoesNotThrow(()->{
            taskDAO.save(new Task( "Task 1", "Desc 1",testUser.getId()));
            taskDAO.save(new Task( "Task 2", "Desc 2", testUser.getId()));
            List<Task> tasks = taskDAO.findAllByUserId(testUser.getId());
            Assertions.assertEquals(2, tasks.size());
        });
    }

    @Test
    @Order(3)
    void testUpdateTask() {
        Assertions.assertDoesNotThrow(()->{
            Task task = taskDAO.save(new Task( "Old Title", "Desc", testUser.getId()));

            task.setTitle("New Title");
            task.setTaskStatus(TaskStatus.IN_PROGRESS);
            taskDAO.update(task);

            Optional<Task> updated = taskDAO.findById(task.getId());
            Assertions.assertTrue(updated.isPresent());
            Assertions.assertEquals("New Title", updated.get().getTitle());
            Assertions.assertEquals(TaskStatus.IN_PROGRESS, updated.get().getTaskStatus());
        });
    }

    @Test
    @Order(4)
    void testCountByUserId() {
        Assertions.assertDoesNotThrow(()->{
            taskDAO.save(new Task( "Task A", "Desc", testUser.getId()));
            taskDAO.save(new Task("Task B", "Desc", testUser.getId()));

            int count = taskDAO.countByUserId(testUser.getId());
            Assertions.assertEquals(2, count);
        });
    }
}
