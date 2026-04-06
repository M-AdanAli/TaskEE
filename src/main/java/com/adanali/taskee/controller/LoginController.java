package com.adanali.taskee.controller;

import com.adanali.taskee.domain.User;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.service.UserService;
import com.adanali.taskee.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginController implements Controller{

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
            return "redirect:/dashboard";
        }

        String errorCode = request.getParameter("error");
        if ("auth".equals(errorCode)) {
            request.setAttribute("errorMessage", "Please login first !");
        }

        return "login";
    }

    private String handleLogin(HttpServletRequest request){
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userService.login(email,password);
            SessionUser sessionUser = new SessionUser(user.getId(), user.getEmail(), user.getFullName());

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("currentUser", sessionUser);

            return "redirect:/dashboard";
        }catch (AuthenticationException e){
            request.setAttribute("errorMessage","Invalid email or password.");
            request.setAttribute("email",email);

            return "login";
        }
    }
}
