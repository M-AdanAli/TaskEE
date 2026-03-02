package com.adanali.taskee.controller;

import com.adanali.taskee.domain.Task;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.service.TaskService;
import com.adanali.taskee.service.TaskServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskListController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(TaskListController.class);
    private final TaskService taskService;

    public TaskListController() {
        this.taskService = new TaskServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // TODO: Security check using Filters
        SessionUser user = (SessionUser) req.getSession().getAttribute("currentUser");
        if (user == null) {
            logger.info("An unknown User tried to fetch tasks.");
            return "redirect:/login";
        }

        logger.info("Tasks Fetching attempt for email: {}", user.email());

        List<Task> userTasks = taskService.getAll(user.id());

        req.setAttribute("taskList", userTasks);
        req.setAttribute("userName", user.fullName());

        return "dashboard";
    }
}
