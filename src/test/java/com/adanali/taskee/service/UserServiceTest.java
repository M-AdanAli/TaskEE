package com.adanali.taskee.service;

import com.adanali.taskee.dao.UserDAO;
import com.adanali.taskee.dao.UserDaoJDBCImpl;
import com.adanali.taskee.domain.User;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private static UserService userService;
    private static UserDAO userDAO; // Direct access for cleanup
    private static final String TEST_EMAIL = "service_test@example.com";

    @BeforeAll
    static void setup() {
        userDAO = new UserDaoJDBCImpl();
        userService = new UserServiceImpl(userDAO);
    }

    @BeforeEach
    void cleanUp() {
        try {
            if (userDAO.exists(TEST_EMAIL)) {
                User u = userDAO.findByEmail(TEST_EMAIL).get();
                userDAO.deleteById(u.getId());
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to clean up", e);
        }

    }

    @Test
    @Order(1)
    void testRegister_HashesPassword() {
        User user = new User(TEST_EMAIL, "plainPassword", "Service Tester");

        User registeredUser = userService.register(user);

        assertNotNull(registeredUser.getId());
        // CRITICAL: The password in DB must NOT match the plain text because of Hashing
        assertNotEquals("plainPassword", registeredUser.getPassword());
        // It should start with BCrypt prefix
        assertTrue(registeredUser.getPassword().startsWith("$2a$"));
    }

    @Test
    @Order(2)
    void testRegister_DuplicateEmail_ThrowsException() {
        userService.register(new User(TEST_EMAIL, "pass", "User 1"));

        User duplicate = new User(TEST_EMAIL, "pass", "User 2");

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(duplicate);
        });
    }

    @Test
    @Order(3)
    void testLogin_Success() {
        userService.register(new User(TEST_EMAIL, "mySecretPass", "Login User"));

        User loggedIn = userService.login(TEST_EMAIL, "mySecretPass");

        assertNotNull(loggedIn);
        assertEquals(TEST_EMAIL, loggedIn.getEmail());
    }

    @Test
    @Order(4)
    void testLogin_WrongPassword_ThrowsException() {
        userService.register(new User(TEST_EMAIL, "correctPass", "Login User"));

        assertThrows(AuthenticationException.class, () -> {
            userService.login(TEST_EMAIL, "wrongPass");
        });
    }
}
