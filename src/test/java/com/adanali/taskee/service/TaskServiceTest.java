package com.adanali.taskee.service;

import com.adanali.taskee.config.DBConnectionManager;
import com.adanali.taskee.dao.TaskDaoJDBCImpl;
import com.adanali.taskee.dao.UserDAO;
import com.adanali.taskee.dao.UserDaoJDBCImpl;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.User;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.exception.AuthorizationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceTest {

    private static TaskService taskService;
    private static UserService userService;
    private static UserDAO userDAO;

    private static final String OWNER_EMAIL = "owner_" + System.currentTimeMillis() + "@test.com";
    private static final String HACKER_EMAIL = "hacker_" + System.currentTimeMillis() + "@test.com";

    private User userOwner;
    private User userHacker;

    @BeforeAll
    static void setup() {
        DBConnectionManager.init();
        userDAO = new UserDaoJDBCImpl();
        userService = new UserServiceImpl(userDAO);
        taskService = new TaskServiceImpl(new TaskDaoJDBCImpl());
    }

    @BeforeEach
    void initData() {
        cleanUser(OWNER_EMAIL);
        cleanUser(HACKER_EMAIL);

        userOwner = userService.register(new User(OWNER_EMAIL, "pass", "Owner"));
        userHacker = userService.register(new User(HACKER_EMAIL, "pass", "Hacker"));
    }

    @AfterAll
    static void tearDown() {
        cleanUser(OWNER_EMAIL);
        cleanUser(HACKER_EMAIL);
    }

    private static void cleanUser(String email) {
        try {
            if (userDAO.exists(email)) {
                userDAO.deleteById(userDAO.findByEmail(email).get().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        Task task = taskService.createTask(new Task("Original", "Desc", userOwner.getId()));

        task.setTitle("Updated Title");
        taskService.updateTask(task);

        Task updated = taskService.getById(task.getId(), userOwner.getId());
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    @Order(3)
    void testUpdateTask_Hacker_AccessDenied() {
        Task task = taskService.createTask(new Task("Secret Data", "Desc", userOwner.getId()));

        Task hackAttempt = new Task();
        hackAttempt.setId(task.getId());
        hackAttempt.setUserId(userHacker.getId()); // Hacker's ID
        hackAttempt.setTitle("HACKED");
        hackAttempt.setTaskStatus(TaskStatus.COMPLETED);

        AuthorizationException ex = assertThrows(AuthorizationException.class, () -> {
            taskService.updateTask(hackAttempt);
        });

        assertTrue(ex.getMessage().contains("Access Denied") || ex.getMessage().contains("authorized"));
    }
}