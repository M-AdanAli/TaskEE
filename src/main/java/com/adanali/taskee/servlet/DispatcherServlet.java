package com.adanali.taskee.servlet;

import com.adanali.taskee.controller.Controller;
import com.adanali.taskee.controller.LoginController;
import com.adanali.taskee.controller.RegistrationController;
import com.adanali.taskee.controller.TaskListController;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.exception.ServiceException;
import com.adanali.taskee.exception.TaskNotFoundException;
import com.adanali.taskee.exception.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "DispatcherServlet",
        urlPatterns = {
                "/login",
                "/logout",
                "/register",
                "/dashboard",
                "/tasks",
                "/tasks/new",
                "/tasks/save",
                "/tasks/edit",
                "/tasks/update",
                "/tasks/delete"
        }
)
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void init(){
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegistrationController());
        controllers.put("/tasks", new TaskListController());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        Controller controller = controllers.get(path);
        if (controller == null){
            response.sendError(404,"Resource not found: "+path);
            return;
        }

        try {
            String viewName = controller.handle(request,response);

            if (viewName.startsWith("redirect:")) {
                String redirectPath = request.getContextPath() + viewName.substring(9);
                response.sendRedirect(redirectPath);
            } else {
                String jspPath = "/WEB-INF/views/" + viewName + ".jsp";
                request.getRequestDispatcher(jspPath).forward(request, response);
            }
        }catch (UserNotFoundException | TaskNotFoundException e) {
            // 404: Resource missing
            response.sendError(404, e.getMessage());
        } catch (AuthorizationException e) {
            // 403: Forbidden (Security)
            response.sendError(403, e.getMessage());
        } catch (ServiceException e) {
            // 500: Generic Business/DB Error
            e.printStackTrace();
            response.sendError(500, "Internal System Error: " + e.getMessage());
        } catch (Exception e) {
            // 500: Unexpected Java Error
            e.printStackTrace();
            throw new ServletException("Unexpected Error", e);
        }
    }
}
