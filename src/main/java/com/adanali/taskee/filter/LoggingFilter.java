package com.adanali.taskee.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();

        if (uri.startsWith(httpRequest.getContextPath() + "/assets")) {
            chain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        LOGGER.info("[START] METHOD:{} URI:{}", method, uri);
        try {
            chain.doFilter(request,response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.info("[END] METHOD:{} URI:{} - Duration: {}ms", method, uri, duration);
        }
    }
}
