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

public class TaskListController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(TaskListController.class);
    private final TaskService taskService;

    public TaskListController() {
        this.taskService = new TaskServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionUser user = (SessionUser) request.getSession().getAttribute("currentUser");

        String statusParameter = request.getParameter("status");
        List<Task> tasks;

        if (statusParameter!=null && !statusParameter.isEmpty()){
            try {
                TaskStatus status = TaskStatus.valueOf(statusParameter);
                tasks = taskService.getTasksByStatus(user.id(), status);
                request.setAttribute("activeFilter", status.name());
            } catch (IllegalArgumentException e) {
                tasks = taskService.getAll(user.id());
                request.setAttribute("activeFilter", "ALL");
            }
        } else {
            logger.info("Tasks Fetching attempt for email: {}", user.email());

            tasks = taskService.getAll(user.id());
            request.setAttribute("activeFilter", "ALL");
        }
        request.setAttribute("taskList", tasks);
        request.setAttribute("userName", user.fullName());

        return "my-tasks";
    }
}
