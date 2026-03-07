package com.adanali.taskee.controller;

import com.adanali.taskee.domain.User;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.service.TaskService;
import com.adanali.taskee.service.TaskServiceImpl;
import com.adanali.taskee.service.UserService;
import com.adanali.taskee.service.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminController implements Controller{

    private final TaskService taskService;
    private final UserService userService;
    private static final String ADMIN_EMAIL = "admin@taskee.com";

    public AdminController() {
        this.taskService = new TaskServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getMethod().equalsIgnoreCase("POST")) {
            return handleLogin(request);
        }

        HttpSession session = request.getSession(false);
        SessionUser user = (session != null) ? (SessionUser) session.getAttribute("currentUser") : null;

        if (user != null && ADMIN_EMAIL.equalsIgnoreCase(user.email())) {
            return loadDashboard(request);
        } else if (user != null && !ADMIN_EMAIL.equalsIgnoreCase(user.email())) {
            throw new AuthorizationException("Access Denied: You do not have administrator privileges.");
        } else {
            return "admin-login";
        }
    }

    private String handleLogin(HttpServletRequest request){
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!ADMIN_EMAIL.equalsIgnoreCase(email)) {
            request.setAttribute("errorMessage", "Access Denied.");
            return "admin-login";
        }

        try {
            User user = userService.login(email,password);

            HttpSession session = request.getSession();
            SessionUser sessionUser = new SessionUser(user.getId(), user.getEmail(), user.getFullName());
            session.setAttribute("currentUser", sessionUser);

            return "redirect:/admin";
        }catch (AuthenticationException e){
            request.setAttribute("errorMessage", "Invalid Credentials.");
            return "admin-login";
        }
    }

    private String loadDashboard(HttpServletRequest request){
        ServletContext context = request.getServletContext();
        AtomicInteger activeUsers = (AtomicInteger) context.getAttribute("activeUserCount");
        LocalDateTime startTime = (LocalDateTime) context.getAttribute("appStartTime");

        Duration uptime = Duration.between(startTime, LocalDateTime.now());
        String uptimeString = String.format("%d hours, %d mins", uptime.toHours(), uptime.toMinutesPart());

        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = (totalMemory - freeMemory) / (1024 * 1024);
        long maxMemory = totalMemory / (1024 * 1024);

        long totalTasks = taskService.countGlobalAllTasks();
        long completedTasks = taskService.countGlobalCompletedTasks();

        request.setAttribute("activeUserCount", activeUsers.get());
        request.setAttribute("uptime",uptimeString);
        request.setAttribute("usedMemory", usedMemory);
        request.setAttribute("maxMemory", maxMemory);
        request.setAttribute("totalTasks", totalTasks);
        request.setAttribute("completedTasks", completedTasks);
        request.setAttribute("completionRate", (totalTasks > 0) ? (completedTasks * 100 / totalTasks) : 0);

        return "admin";
    }
}
