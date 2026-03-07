<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">

        <style>
            .admin-header { background: var(--primary-dark); color: white; padding: 2rem; border-radius: var(--radius-md); margin-bottom: 30px; }
            .metric-card { background: white; padding: 1.5rem; border-radius: var(--radius-md); border-left: 5px solid var(--primary-dark); box-shadow: var(--shadow-subtle); }
            .metric-val { font-family: var(--font-code); font-size: 2rem; font-weight: 700; color: var(--primary-dark); }
            .metric-label { color: var(--text-muted); font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em; }
            .progress-container {background-color: #EDF2F7;border-radius: 20px; height: 24px;width: 100%;margin: 15px 0;overflow: hidden;}
            .progress-fill {background-color: var(--success-color); /* Teal */height: 100%;border-radius: 20px 0 0 20px;transition: width 1s ease-in-out;display: flex;align-items: center;justify-content: flex-end;padding-right: 10px;font-size: 0.75rem;color: white;font-weight: 700;}
            .stat-row {display: flex;justify-content: space-between;font-size: 0.9rem;color: var(--text-muted);font-family: var(--font-code);}
        </style>
    </head>
    <body>

        <nav class="navbar">
            <div class="nav-brand">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                     alt="TaskEE Logo"
                     style="height: 50px; width: auto;">
                <span>Admin</span>
            </div>

            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-secondary" style="border: none; color: var(--text-main)">Exit to App</a>
            </div>
        </nav>

        <div class="container">

            <div class="admin-header">
                <h1 style="color: white; margin-bottom: 10px;">System Health</h1>
                <p style="color: #a0aec0;">Real-time monitoring of application performance.</p>
            </div>

            <div class="stats-grid">

                <div class="metric-card" style="border-color: #38B2AC;">
                    <div class="metric-label">Active Sessions</div>
                    <div class="metric-val">${activeUserCount}</div>
                    <small style="color: #38B2AC;">Live Users</small>
                </div>

                <div class="metric-card" style="border-color: #F78C29;">
                    <div class="metric-label">System Uptime</div>
                    <div class="metric-val" style="font-size: 1.5rem;">${uptime}</div>
                    <small style="color: #F78C29;">Since Restart</small>
                </div>

                <div class="metric-card" style="border-color: #E53E3E;">
                    <div class="metric-label">Memory Usage</div>
                    <div class="metric-val">${usedMemory} <span style="font-size: 1rem;">MB</span></div>
                    <small style="color: var(--text-muted);">of ${maxMemory} MB Allocated</small>
                </div>

            </div>

            <div style="margin-top: 30px;">
                <h3 style="margin-bottom: 15px; color: var(--primary-dark);">Platform Productivity</h3>

                <div class="card">
                    <div style="display: flex; justify-content: space-between; align-items: flex-end;">
                        <div>
                            <h4 style="margin: 0; color: var(--text-muted); font-weight: 600;">Global Completion Rate</h4>
                            <span style="font-size: 2.5rem; font-weight: 700; color: var(--primary-dark); font-family: var(--font-heading);">
                        ${completionRate}%
                    </span>
                        </div>
                        <div style="text-align: right;">
                            <div style="font-size: 0.9rem; color: var(--text-muted);">Total Tasks</div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--primary-dark);">${totalTasks}</div>
                        </div>
                    </div>
                    <div class="progress-container">
                        <div class="progress-fill" style="width: ${completionRate}%;">
                            <!-- Show % inside bar if it's wide enough, otherwise hide text -->
                            <c:if test="${completionRate > 10}">${completionRate}%</c:if>
                        </div>
                    </div>
                    <div class="stat-row">
                <span>
                    <span style="color: var(--success-color);">●</span> ${completedTasks} Completed
                </span>
                        <span>
                    <span style="color: #4A5568;">●</span> ${totalTasks - completedTasks} Active (Pending/In Progress)
                </span>
                    </div>
                </div>
            </div>

        </div>
    </body>
</html>