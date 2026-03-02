package com.adanali.taskee.controller;

import com.adanali.taskee.domain.User;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.service.UserService;
import com.adanali.taskee.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    public LoginController(){
        this.userService = new UserServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equalsIgnoreCase("POST")){
            return handleLogin(request);
        }

        if (request.getSession().getAttribute("currentUser") != null){
            return "redirect:/tasks";
        }

        return "login";
    }

    private String handleLogin(HttpServletRequest request){
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.info("Login attempt for email: {}",email);

        try {
            User user = userService.login(email,password);
            SessionUser sessionUser = new SessionUser(user.getId(), user.getEmail(), user.getFullName());

            request.getSession().setAttribute("currentUser", sessionUser);

            logger.info("User {} logged in successfully.", user.getEmail());
            return "redirect:/tasks";
        }catch (AuthenticationException e){
            logger.warn("Login attempt failed for {}: {}",email, e.getMessage());
            request.setAttribute("errorMessage","Invalid email or password.");
            request.setAttribute("email",email);

            return "login";
        }
    }
}
