package com.adanali.taskee.service;

import com.adanali.taskee.dao.TaskDaoJDBCImpl;
import com.adanali.taskee.dao.UserDAO;
import com.adanali.taskee.dao.UserDaoJDBCImpl;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.User;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.exception.ServiceException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceTest {

    private static TaskService taskService;
    private static UserService userService;
    private static UserDAO userDAO;

    private User userOwner;
    private User userHacker;

    @BeforeAll
    static void setup() {
        userDAO = new UserDaoJDBCImpl();
        userService = new UserServiceImpl(userDAO);
        taskService = new TaskServiceImpl(new TaskDaoJDBCImpl());
    }

    @BeforeEach
    void initData() {
        cleanUser("owner@test.com");
        cleanUser("hacker@test.com");

        userOwner = userService.register(new User("owner@test.com", "pass", "Owner"));
        userHacker = userService.register(new User("hacker@test.com", "pass", "Hacker"));
    }

    private void cleanUser(String email) {
        try {
            if (userDAO.exists(email)) {
                userDAO.deleteById(userDAO.findByEmail(email).get().getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean up");
        }
    }

    @Test
    @Order(1)
    void testCreateTask_Success() {
        Task task = new Task("My Task", "Desc", userOwner.getId());
        Task created = taskService.createTask(task);

        assertNotNull(created.getId());
        assertEquals(userOwner.getId(), created.getUserId());
    }

    @Test
    @Order(2)
    void testUpdateTask_Owner_Success() {
        // 1. Create task as Owner
        Task task = taskService.createTask(new Task("Original", "Desc", userOwner.getId()));

        // 2. Prepare update
        task.setTitle("Updated Title");
        taskService.updateTask(task);

        // 3. Verify
        Task updated = taskService.getById(task.getId(), userOwner.getId());
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    @Order(3)
    void testUpdateTask_Hacker_AccessDenied() {
        // 1. Owner creates a task
        Task task = taskService.createTask(new Task("Secret Data", "Desc", userOwner.getId()));

        // 2. Hacker tries to update Owner's task
        // We simulate this by creating a task object with the Owner's Task ID but Hacker's User ID
        Task hackAttempt = new Task();
        hackAttempt.setId(task.getId());
        hackAttempt.setUserId(userHacker.getId()); // <--- The Hacker's Session ID
        hackAttempt.setTitle("HACKED");
        hackAttempt.setTaskStatus(TaskStatus.COMPLETED);

        // 3. Expect AuthorizationException (Access Denied)
        ServiceException ex = assertThrows(AuthorizationException.class, () -> {
            taskService.updateTask(hackAttempt);
        });

        assertTrue(ex.getMessage().contains("Access Denied") || ex.getMessage().contains("not own"));
    }
}