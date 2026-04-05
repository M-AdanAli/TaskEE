package com.adanali.taskee.controller;

import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.service.TaskService;
import com.adanali.taskee.service.TaskServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DashboardController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final TaskService taskService;

    public DashboardController() {
        this.taskService = new TaskServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionUser user = (SessionUser) request.getSession().getAttribute("currentUser");

        logger.info("Dashboard Metrics fetching attempt for email: {}", user.email());

        long pendingCount = taskService.getTaskCountByStatus(user.id(), TaskStatus.PENDING);
        long progressCount = taskService.getTaskCountByStatus(user.id(), TaskStatus.IN_PROGRESS);
        long completedCount = taskService.getTaskCountByStatus(user.id(), TaskStatus.COMPLETED);
        long totalTasks = pendingCount + progressCount + completedCount;

        request.setAttribute("totalTasks", totalTasks);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("progressCount", progressCount);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("userName", user.fullName());

        List<Task> recentTasks = taskService.getAll(user.id(), 1, 5).items();
        request.setAttribute("recentTasks", recentTasks);

        return "dashboard";
    }
}
