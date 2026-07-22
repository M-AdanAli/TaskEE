package com.adanali.taskee.controller;

import com.adanali.taskee.domain.enums.Role;
import com.adanali.taskee.dto.Page;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.dto.UserSummary;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.service.TaskService;
import com.adanali.taskee.service.TaskServiceImpl;
import com.adanali.taskee.service.UserService;
import com.adanali.taskee.service.UserServiceImpl;
import com.adanali.taskee.util.SessionRegistry;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminController implements Controller{

    private final TaskService taskService;
    private final UserService userService;
    private static final int PAGE_SIZE = 10;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController() {
        this.taskService = new TaskServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        SessionUser user = (session != null) ? (SessionUser) session.getAttribute("currentUser") : null;

        if (user != null && user.role().equals(Role.ADMIN)) {
            String completePath = request.getServletPath() +
                    (request.getPathInfo() != null ? request.getPathInfo() : "");

            return switch (completePath) {
                case "/admin" -> loadDashboard(request);
                case "/admin/users" -> loadUsersList(request, user);
                case "/admin/users/status" -> updateUserStatus(request, user);
                default -> "redirect:/admin";
            };
        } else {
            throw new AuthorizationException("Only Admins Allowed");
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

    private String loadUsersList(HttpServletRequest request, SessionUser requester){
        if (request.getMethod().equalsIgnoreCase("GET")){
            int page = 1;
            String pageParameter = request.getParameter("page");
            if (pageParameter!=null && !pageParameter.isBlank()){
                try {
                    page = Integer.parseInt(pageParameter);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e){
                    logger.error("Could not parse 'page' Parameter. Defaulted to '1'.");
                }
            }

            Page<UserSummary> userSummaries = userService.getAll(requester, page, PAGE_SIZE);
            request.setAttribute("userSummariesPage", userSummaries);
            request.setAttribute("offset", (page-1) * PAGE_SIZE);
            return "admin-users";
        }else return "redirect:/admin";
    }

    private String updateUserStatus(HttpServletRequest request, SessionUser requester){
        if (request.getMethod().equalsIgnoreCase("POST")){
            Long targetId = Long.parseLong(request.getParameter("id"));
            boolean newStatus = Boolean.parseBoolean(request.getParameter("status"));
            int sourcePage;
            try {
                sourcePage = Integer.parseInt(request.getParameter("sourcePage"));
            }catch (Exception e){
                sourcePage = 1;
            }


            try {
                userService.updateStatus(targetId, newStatus, requester);
                if (!newStatus) {
                    SessionRegistry.invalidateUserSessions(targetId);
                }
            } catch (AuthorizationException e) {
                request.getSession().setAttribute("flashError", e.getMessage());
            }
            return "redirect:/admin/users?page="+sourcePage;
        }
        return "redirect:/admin/users";
    }
}
