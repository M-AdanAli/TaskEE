package com.adanali.taskee.controller;

import com.adanali.taskee.domain.enums.Role;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.service.TaskService;
import com.adanali.taskee.service.TaskServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminController implements Controller{

    private final TaskService taskService;

    public AdminController() {
        this.taskService = new TaskServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        SessionUser user = (session != null) ? (SessionUser) session.getAttribute("currentUser") : null;

        if (user != null && user.role().equals(Role.ADMIN)) {
            return loadDashboard(request);
        } else {
            throw new AuthorizationException();
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
