package com.adanali.taskee.dao;

import com.adanali.taskee.config.DBConnectionManager;
import com.adanali.taskee.domain.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private static UserDAO userDAO;
    private static final String TEST_EMAIL = "test_user_" + System.currentTimeMillis() + "@example.com";
    private static final String TEST_EMAIL_2 = "test_email2_" + System.currentTimeMillis() + "@example.com";
    private static Long testId;
    private static Long testId2;

    @BeforeAll
    static void setup() {
        DBConnectionManager.init();
        userDAO = new UserDaoJDBCImpl();
    }

    @AfterAll
    static void tearDown() {
        Assertions.assertDoesNotThrow(() -> {
            if (testId != null) userDAO.deleteById(testId);
            if (testId2 != null) userDAO.deleteById(testId2);
        });
    }

    @Test
    @Order(1)
    void testSaveUser() {
        User user = new User(TEST_EMAIL, "secret123", "Test User");
        Assertions.assertDoesNotThrow(()->{
            User savedUser = userDAO.save(user);
            Assertions.assertNotNull(savedUser.getId(), "ID should be generated");
            testId = savedUser.getId();
            Assertions.assertEquals(TEST_EMAIL, savedUser.getEmail());
        });
    }

    @Test
    @Order(2)
    void testFindByEmail() {
        Assertions.assertDoesNotThrow(()->{
            Optional<User> found = userDAO.findByEmail(TEST_EMAIL);
            Assertions.assertTrue(found.isPresent());
            Assertions.assertEquals("Test User", found.get().getFullName());
        });
    }

    @Test
    @Order(3)
    void testFindById() {
        Assertions.assertDoesNotThrow(()->{
            Optional<User> found = userDAO.findById(testId);
            Assertions.assertTrue(found.isPresent());
            Assertions.assertEquals("Test User", found.get().getFullName());
        });
    }

    @Test
    @Order(4)
    void testExists() {
        Assertions.assertDoesNotThrow(()->{
            Assertions.assertTrue(userDAO.exists(TEST_EMAIL));
            Assertions.assertFalse(userDAO.exists("fake_nonexistent@email.com"));
        });
    }

    @Test
    @Order(5)
    void testFindAll() {
        User user = new User(TEST_EMAIL_2, "secret123", "Test User 2");
        Assertions.assertDoesNotThrow(()->{
            User savedUser = userDAO.save(user);
            testId2 = savedUser.getId();
        });
        Assertions.assertDoesNotThrow(()-> {
            List<User> users = userDAO.findAll();
            // FIX: Index-agnostic search (Works safely on databases with 1 or 1,000,000 existing users)
            boolean userFound = users.stream().anyMatch(u -> u.getEmail().equals(TEST_EMAIL_2));
            Assertions.assertTrue(userFound, "Newly created user should exist in the findAll list.");
        });
    }

    @Test
    @Order(6)
    void testUpdate() {
        Assertions.assertDoesNotThrow(()->{
            Optional<User> found = userDAO.findByEmail(TEST_EMAIL);
            Assertions.assertTrue(found.isPresent());
            User userToUpdate = found.get();
            userToUpdate.setFullName("Updated Test User Name");
            userDAO.update(userToUpdate);

            User updatedUser = userDAO.findByEmail(TEST_EMAIL).get();
            Assertions.assertEquals("Updated Test User Name", updatedUser.getFullName());
        });
    }

    @Test
    @Order(7)
    void testDelete() {
        Assertions.assertDoesNotThrow(()->{
            userDAO.deleteById(testId);
            Optional<User> optionalUser = userDAO.findById(testId);
            Assertions.assertTrue(optionalUser.isEmpty());
            testId = null;
        });
    }
}
