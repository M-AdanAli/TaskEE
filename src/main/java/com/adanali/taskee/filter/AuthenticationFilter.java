package com.adanali.taskee.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login",
            "/register",
            "/logout",
            "/assets",
            "/index.jsp",
            "/admin"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);

        if (isLoggedIn) {
            filterChain.doFilter(request, response);
        } else {
            logger.warn("Unauthorized access attempt to: {} from IP: {}", path, request.getRemoteAddr());
            response.sendRedirect(request.getContextPath() + "/login?error=auth");
        }
    }
}
