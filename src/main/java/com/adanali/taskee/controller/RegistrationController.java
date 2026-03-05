package com.adanali.taskee.controller;

import com.adanali.taskee.domain.User;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.exception.UserAlreadyExistsException;
import com.adanali.taskee.service.UserService;
import com.adanali.taskee.service.UserServiceImpl;
import com.adanali.taskee.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserService userService;

    public RegistrationController(){
        this.userService = new UserServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getMethod().equalsIgnoreCase("POST")){
            return handleRegistration(request);
        }

        if (request.getSession().getAttribute("currentUser") != null){
            return "redirect:/dashboard";
        }

        return "register";
    }

    private String handleRegistration(HttpServletRequest request){
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.info("Registration attempt for email: {}", email);

        if (fullName == null || fullName.isBlank()) {
            logger.warn("Registration failed: Blank Full Name format for {}", email);
            return showError(request, "Full Name is required.", fullName, email);
        }

        if (!ValidationUtil.isValidEmail(email)) {
            logger.warn("Registration failed: Invalid email format for {}", email);
            return showError(request, "Please enter a valid email address.", fullName, email);
        }

        if (!ValidationUtil.isValidPassword(password)) {
            logger.warn("Registration failed: Weak password for {}", email);
            return showError(request, "Password must be 8-20 chars including,\n - Uppercase\n- Lowercase\n- Number\n- Special Character.", fullName, email);
        }

        try {
            User newUser = new User(email, password, fullName);
            User registeredUser = userService.register(newUser);
            SessionUser sessionUser = new SessionUser(registeredUser.getId(), registeredUser.getEmail(), registeredUser.getFullName());

            request.getSession().setAttribute("currentUser", sessionUser);

            logger.info("User registered and logged in: {}", email);
            return "redirect:/dashboard";
        } catch (UserAlreadyExistsException e) {
            logger.warn("Registration failed: Email {} already exists.", email);
            return showError(request, "The email is already registered.", fullName, email);
        }
    }

    private String showError(HttpServletRequest request, String message, String name, String email) {
        request.setAttribute("errorMessage", message);
        request.setAttribute("fullName", name);
        request.setAttribute("email", email);
        return "register";
    }
}
