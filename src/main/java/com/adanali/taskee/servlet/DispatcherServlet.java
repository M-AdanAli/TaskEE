package com.adanali.taskee.servlet;

import com.adanali.taskee.controller.*;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.exception.ServiceException;
import com.adanali.taskee.exception.TaskNotFoundException;
import com.adanali.taskee.exception.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "DispatcherServlet",
        urlPatterns = {
                "/login",
                "/logout",
                "/register",
                "/profile",
                "/profile/update",
                "/profile/password",
                "/dashboard",
                "/tasks",
                "/tasks/new",
                "/tasks/save",
                "/tasks/edit",
                "/tasks/update",
                "/tasks/status",
                "/tasks/delete",
                "/admin"
        }
)
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllers = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(){
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegistrationController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/dashboard", new DashboardController());
        controllers.put("/tasks", new TaskListController());

        TaskController taskController = new TaskController();
        controllers.put("/tasks/new", taskController);
        controllers.put("/tasks/save", taskController);
        controllers.put("/tasks/edit", taskController);
        controllers.put("/tasks/update", taskController);
        controllers.put("/tasks/status", taskController);
        controllers.put("/tasks/delete", taskController);

        ProfileController profileController = new ProfileController();
        controllers.put("/profile", profileController);
        controllers.put("/profile/update", profileController);
        controllers.put("/profile/password", profileController);

        controllers.put("/admin", new AdminController());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        Controller controller = controllers.get(path);
        if (controller == null){
            LOGGER.warn("Resource Not Found at {}", path);
            response.sendError(404,"Resource not found: "+path);
            return;
        }

        try {
            String viewName = controller.handle(request,response);

            if (viewName.startsWith("redirect:")) {
                String redirectPath = request.getContextPath() + viewName.substring(9);
                response.sendRedirect(redirectPath);
            } else {
                setActivePage(request, viewName);
                String jspPath = "/WEB-INF/views/" + viewName + ".jsp";
                request.getRequestDispatcher(jspPath).forward(request, response);
            }
        }catch (UserNotFoundException | TaskNotFoundException e) {
            LOGGER.warn("Resource Not Found at {}: {}", path, e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            LOGGER.warn("SECURITY: Unauthorized access to {} | {}", path, e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (ServiceException e) {
            LOGGER.error("System Error during request to {}: {}", path, e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal System Error");
        } catch (Exception e) {
            LOGGER.error("Unexpected Application Crash on {}: {}", path, e.getMessage(), e);
            throw new ServletException("Unexpected Error", e);
        }
    }

    private void setActivePage(HttpServletRequest request, String viewName) {
        switch (viewName) {
            case "dashboard":
                request.setAttribute("activePage", "dashboard");
                break;
            case "my-tasks":
            case "task-form":
                request.setAttribute("activePage", "tasks");
                break;
            case "profile":
                request.setAttribute("activePage", "profile");
                break;
            default:
                break;
        }
    }
}
