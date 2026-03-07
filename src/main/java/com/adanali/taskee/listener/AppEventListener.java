package com.adanali.taskee.listener;

import com.adanali.taskee.config.DBConnectionManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class AppEventListener implements ServletContextListener, HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(AppEventListener.class);
    private final AtomicInteger activeSessions = new AtomicInteger(0);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        LocalDateTime startTime = LocalDateTime.now();
        context.setAttribute("appStartTime", startTime);
        context.setAttribute("activeUserCount", activeSessions);

        try {
            DBConnectionManager.init();
            logger.info("✅ Database Connection Pool Initialized Successfully");
        } catch (Exception e) {
            logger.error("❌ CRITICAL: Database Connection Pool Failed to Start", e);
            throw new RuntimeException("Database Connection Pool Failed to Start",e);
        }

        logger.info("🚀 TaskEE Application Started at: {}", startTime);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnectionManager.shutdown();

        logger.info("🛑 TaskEE Application Stopped. Resources released.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int currentCount = activeSessions.incrementAndGet();

        logger.info("Session Created: {}. Total Active Sessions: {}", se.getSession().getId(), currentCount);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int currentCount = activeSessions.decrementAndGet();

        logger.info("Session Destroyed: {}. Total Active Sessions: {}", se.getSession().getId(), currentCount);
    }
}
