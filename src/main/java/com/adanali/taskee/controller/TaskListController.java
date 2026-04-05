package com.adanali.taskee.controller;

import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.dto.Page;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.service.TaskService;
import com.adanali.taskee.service.TaskServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskListController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(TaskListController.class);
    private final TaskService taskService;
    private static final int PAGE_SIZE = 6;

    public TaskListController() {
        this.taskService = new TaskServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionUser user = (SessionUser) request.getSession().getAttribute("currentUser");

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

        String statusParameter = request.getParameter("status");
        Page<Task> tasksPage;

        if (statusParameter!=null && !statusParameter.isEmpty()){
            try {
                TaskStatus status = TaskStatus.valueOf(statusParameter);
                tasksPage = taskService.getTasksByStatus(user.id(), status, page, PAGE_SIZE);
                request.setAttribute("activeFilter", status.name());
            } catch (IllegalArgumentException e) {
                tasksPage = taskService.getAll(user.id(), page, PAGE_SIZE);
                request.setAttribute("activeFilter", "ALL");
            }
        } else {
            logger.info("Tasks Fetching attempt for email: {}", user.email());

            tasksPage = taskService.getAll(user.id(), page, PAGE_SIZE);
            request.setAttribute("activeFilter", "ALL");
        }
        request.setAttribute("taskPage", tasksPage);
        request.setAttribute("offset", (page-1) * PAGE_SIZE);
        request.setAttribute("userName", user.fullName());

        return "my-tasks";
    }
}
