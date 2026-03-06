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
        List<Task> allTasks = taskService.getAll(user.id());

        long pendingCount = allTasks.stream().filter(t -> t.getTaskStatus() == TaskStatus.PENDING).count();
        long progressCount = allTasks.stream().filter(t -> t.getTaskStatus() == TaskStatus.IN_PROGRESS).count();
        long completedCount = allTasks.stream().filter(t -> t.getTaskStatus() == TaskStatus.COMPLETED).count();

        request.setAttribute("totalTasks", allTasks.size());
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("progressCount", progressCount);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("userName", user.fullName());

        List<Task> recentTasks = (allTasks.size() > 5 ) ? allTasks.subList(0, 5) : allTasks;
        request.setAttribute("recentTasks", recentTasks);

        return "dashboard";
    }
}
