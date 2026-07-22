<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>System Health | TaskEE Admin</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&family=JetBrains+Mono:wght@400&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

        <div class="container">

            <!-- Unified Admin Header -->
            <div class="admin-header">
                <h1 style="color: white; margin-bottom: 10px;">⚡ Admin Console</h1>
                <p style="color: #a0aec0; margin: 0;">Global platform control and monitoring.</p>
            </div>

            <!-- SUB-NAV TABS (System Health is Active) -->
            <div class="sub-nav">
                <a href="${pageContext.request.contextPath}/admin" class="sub-nav-tab active">System Health</a>
                <a href="${pageContext.request.contextPath}/admin/users" class="sub-nav-tab">User Management</a>
            </div>

            <!-- System Health Metrics -->
            <div class="stats-grid">
                <div class="metric-card" style="border-color: #38B2AC;">
                    <div class="metric-label">Active Sessions</div>
                    <div class="metric-val"><c:out value="${activeUserCount}"/></div>
                    <small style="color: #38B2AC;">Live Users</small>
                </div>

                <div class="metric-card" style="border-color: #F78C29;">
                    <div class="metric-label">System Uptime</div>
                    <div class="metric-val" style="font-size: 1.5rem;"><c:out value="${uptime}"/></div>
                    <small style="color: #F78C29;">Since Restart</small>
                </div>

                <div class="metric-card" style="border-color: #E53E3E;">
                    <div class="metric-label">Memory Usage</div>
                    <div class="metric-val"><c:out value="${usedMemory}"/> <span style="font-size: 1rem;">MB</span></div>
                    <small class="text-muted">of <c:out value="${maxMemory}"/> MB Allocated</small>
                </div>
            </div>

            <!-- Productivity Metrics -->
            <div class="mt-4">
                <h3 style="margin-bottom: 15px; color: var(--primary-dark);">Platform Productivity</h3>

                <div class="card">
                    <div class="d-flex flex-between align-end">
                        <div>
                            <h4 class="text-muted font-bold" style="margin: 0;">Global Completion Rate</h4>
                            <span style="font-size: 2.5rem; font-weight: 700; color: var(--primary-dark); font-family: var(--font-heading);">
                            <c:out value="${completionRate}"/>%
                        </span>
                        </div>
                        <div class="text-right">
                            <div class="text-muted" style="font-size: 0.9rem;">Total Tasks</div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--primary-dark);">
                                <c:out value="${totalTasks}"/>
                            </div>
                        </div>
                    </div>

                    <div class="progress-container">
                        <div class="progress-fill" style="width: ${completionRate}%;">
                            <c:if test="${completionRate > 10}"><c:out value="${completionRate}"/>%</c:if>
                        </div>
                    </div>

                    <div class="stat-row">
                    <span>
                        <span style="color: var(--success-color);">●</span> <c:out value="${completedTasks}"/> Completed
                    </span>
                        <span>
                        <span style="color: #4A5568;">●</span> <c:out value="${totalTasks - completedTasks}"/> Active
                    </span>
                    </div>
                </div>
            </div>

        </div>
    </body>
</html>