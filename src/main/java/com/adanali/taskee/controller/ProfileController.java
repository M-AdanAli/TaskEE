package com.adanali.taskee.controller;

import com.adanali.taskee.domain.User;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.service.UserService;
import com.adanali.taskee.service.UserServiceImpl;
import com.adanali.taskee.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileController implements Controller{

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final UserService userService;

    public ProfileController() {
        this.userService = new UserServiceImpl();
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionUser user = (SessionUser) request.getSession().getAttribute("currentUser");

        String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");

        if (request.getMethod().equalsIgnoreCase("POST")) {
            if (path.equals("/profile/update")) {
                return updateProfile(request, user);
            } else if (path.equals("/profile/password")) {
                return changePassword(request, user);
            }
        }

        return "profile";
    }

    private String updateProfile(HttpServletRequest request, SessionUser currentUser) {
        String fullName = request.getParameter("fullName");
        logger.info("Received a request to update Full Name for {}",currentUser.email());

        if (fullName == null || fullName.isBlank() || fullName.length() > 100) {
            logger.warn("Failed to update Full Name: Incorrect Full Name for {}", currentUser.email());
            request.setAttribute("errorProfile", "Name is required and must be under 100 chars.");
            return "profile";
        }

        if (fullName.equals(currentUser.fullName())){
            logger.warn("Failed to update Full Name: No change in the new Name for user {}", currentUser.email());
            request.setAttribute("errorProfile", "Please Enter a new Name");
            return "profile";
        }

        User userToUpdate = new User();
        userToUpdate.setId(currentUser.id());
        userToUpdate.setFullName(fullName);

        userService.updateProfile(userToUpdate);

        SessionUser updatedSessionUser = new SessionUser(currentUser.id(), currentUser.email(), fullName);
        request.getSession().setAttribute("currentUser", updatedSessionUser);

        logger.warn("Full Name updated successfully for {}", currentUser.email());
        request.setAttribute("successProfile", "Profile updated successfully.");
        return "profile";
    }

    private String changePassword(HttpServletRequest request, SessionUser currentUser) {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        if (!ValidationUtil.isValidPassword(newPassword)) {
            logger.warn("Failed to update Password: Weak password {} for {}",newPassword, currentUser.email());
            request.setAttribute("errorPassword", "Password must be 8-20 chars including,\n - Uppercase\n- Lowercase\n- Number\n- Special Character.");
            return "profile";
        }

        if (newPassword.equals(currentPassword)){
            logger.warn("Failed to update Password: No change in the new Password for user {}", currentUser.email());
            request.setAttribute("errorPassword", "Please Enter a new Password.");
            return "profile";
        }

        try {
            userService.changePassword(currentUser.id(), currentPassword, newPassword);
            logger.info("Password updated successfully for {}", currentUser.email());
            request.setAttribute("successPassword", "Password changed successfully.");
        } catch (AuthenticationException e) {
            logger.warn("Failed to update Password: Current password {} is incorrect for {}",currentPassword, currentUser.email());
            request.setAttribute("errorPassword", "Current password is incorrect.");
        }

        return "profile";
    }
}
