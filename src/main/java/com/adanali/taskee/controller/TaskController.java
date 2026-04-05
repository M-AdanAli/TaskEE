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

public class TaskController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController() {
        this.taskService = new TaskServiceImpl();
    }
    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionUser user = (SessionUser) request.getSession().getAttribute("currentUser");

        String completePath = request.getServletPath() +
                (request.getPathInfo() != null ? request.getPathInfo() : "");
        return switch (completePath) {
            case "/tasks/new" -> showCreateForm(request);
            case "/tasks/save" -> saveTask(request, user.id());
            case "/tasks/edit" -> showEditForm(request, user.id());
            case "/tasks/update" -> updateTask(request, user.id());
            case "/tasks/status" -> updateStatus(request, user.id());
            case "/tasks/delete" -> deleteTask(request, user.id());
            default -> "redirect:/tasks";
        };
    }

    private String showCreateForm(HttpServletRequest request) {
        request.setAttribute("pageTitle", "New Task");
        return "task-form";
    }

    private String showEditForm(HttpServletRequest request, Long userId) {
        String idParam = request.getParameter("id");
        if (idParam == null) return "redirect:/tasks";

        Long taskId = Long.parseLong(idParam);
        Task task = taskService.getById(taskId, userId);

        request.setAttribute("task", task);
        request.setAttribute("pageTitle", "Edit Task");
        return "task-form";
    }

    private String saveTask(HttpServletRequest request, Long userId) {
        if (!request.getMethod().equalsIgnoreCase("POST")) return "redirect:/tasks";

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String statusString = request.getParameter("status");

        if (title == null || title.isBlank() || title.length() > 150) {
            return showFormWithError(request, "Title is required and must be under 150 characters.", title, description, null);
        }

        if (description != null && description.length() > 5000) {
            return showFormWithError(request, "Description is too long (max 5000 characters).", title, description, null);
        }

        Task task = new Task(title, description, userId);

        if (statusString != null && (statusString.equalsIgnoreCase("PENDING") ||
                                     statusString.equalsIgnoreCase("IN_PROGRESS"))
        ) {
                task.setTaskStatus(TaskStatus.valueOf(statusString));
        }

        taskService.createTask(task);

        String source = request.getParameter("source");
        if (source != null && source.equalsIgnoreCase("dashboard")) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/tasks";
        }
    }

    private String updateTask(HttpServletRequest request, Long userId) {
        if (!request.getMethod().equalsIgnoreCase("POST")) return "redirect:/tasks";

        Long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String statusString = request.getParameter("status");

        if (title == null || title.isBlank() || title.length() > 150) {
            return showFormWithError(request, "Title is required and must be under 150 characters.", title, description, id);
        }

        if (description != null && description.length() > 5000) {
            return showFormWithError(request, "Description is too long (max 5000 characters).", title, description, id);
        }

        Task task = new Task();
        task.setId(id);
        task.setUserId(userId);
        task.setTitle(title);
        task.setDescription(description);

        if (statusString != null &&
                (statusString.equalsIgnoreCase("PENDING") ||
                 statusString.equalsIgnoreCase("IN_PROGRESS") ||
                 statusString.equalsIgnoreCase("COMPLETED")
                )
        ) {
            task.setTaskStatus(TaskStatus.valueOf(statusString));
        }

        taskService.updateTask(task);

        String source = request.getParameter("source");
        if (source != null && source.equalsIgnoreCase("dashboard")) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/tasks";
        }
    }

    private String updateStatus(HttpServletRequest request, Long userId) {
        if (!request.getMethod().equalsIgnoreCase("POST")) return "redirect:/tasks";

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String statusString = request.getParameter("status");
            TaskStatus newStatus = TaskStatus.valueOf(statusString);

            taskService.updateTaskStatus(id, userId, newStatus);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null && referer.contains("dashboard") ? "/dashboard" : "/tasks");
    }

    private String deleteTask(HttpServletRequest request, Long userId) {
        if (!request.getMethod().equalsIgnoreCase("POST")) return "redirect:/tasks";

        Long id = Long.parseLong(request.getParameter("id"));
        taskService.delete(id, userId);

        String source = request.getParameter("source");
        if (source.equalsIgnoreCase("dashboard")) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/tasks";
        }
    }

    private String showFormWithError(HttpServletRequest request, String error, String title, String desc, Long id) {
        request.setAttribute("errorMessage", error);

        Task tempTask = new Task();
        if (id != null) tempTask.setId(id);
        tempTask.setTitle(title);
        tempTask.setDescription(desc);

        request.setAttribute("task", tempTask);
        request.setAttribute("pageTitle", id == null ? "Create New Task" : "Edit Task");

        return "task-form";
    }
}
