package com.adanali.taskee.dao;

import com.adanali.taskee.domain.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private static UserDAO userDAO;
    private static final String TEST_EMAIL = "test_user@example.com";
    private static Long testId;

    @BeforeAll
    static void setup() {
        userDAO = new UserDaoJDBCImpl();
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
            Assertions.assertFalse(userDAO.exists("fake@email.com"));
        });
    }

    @Test
    @Order(5)
    void testFindAll() {
        User user = new User("test_email_2@example.com", "secret123", "Test User 2");
        Assertions.assertDoesNotThrow(()->{
            User savedUser = userDAO.save(user);
            Assertions.assertNotNull(savedUser.getId(), "ID should be generated");
            testId = savedUser.getId();
            Assertions.assertEquals("test_email_2@example.com", savedUser.getEmail());
        });
        Assertions.assertDoesNotThrow(()-> {
            List<User> users = userDAO.findAll();
            User user2 = users.get(1);
            Assertions.assertEquals("Test User 2", user2.getFullName());
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
            Optional<User> found1 = userDAO.findByEmail(TEST_EMAIL);
            Assertions.assertTrue(found1.isPresent());
            User updatedUser = found1.get();
            Assertions.assertEquals("Updated Test User Name", updatedUser.getFullName());
        });
    }

    @Test
    @Order(7)
    void testDelete() {
        Assertions.assertDoesNotThrow(()->{
            User userToDelete = userDAO.findByEmail(TEST_EMAIL).get();
            userDAO.deleteById(userToDelete.getId());
            Optional<User> optionalUser = userDAO.findById(userToDelete.getId());
            Assertions.assertEquals(optionalUser, Optional.empty());
        });
    }
}
