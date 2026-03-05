<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>My Tasks | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body>

        <nav class="navbar">
            <div class="nav-brand">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                     alt="TaskEE Logo"
                     style="height: 50px; width: auto;">
                <span>TaskEE</span>
            </div>

            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a>
                <a href="${pageContext.request.contextPath}/tasks">My Tasks</a>
                <a href="${pageContext.request.contextPath}/profile">Profile</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-secondary" style="border: none; color: var(--text-main)">Logout</a>
            </div>
        </nav>

        <div class="container">

            <div style="text-align: center; margin: 40px 0 60px 0;">
                <h1 style="font-size: 2.5rem;">Welcome back, <span class="hero-name">${userName}</span>!</h1>
                <p style="color: var(--text-muted); font-size: 1.3rem; letter-spacing: -0.02em;">
                    Here's your productivity overview...
                </p>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-label">Total Tasks</div>
                    <div class="stat-number">${totalTasks}</div>
                </div>
                <div class="stat-card stat-pending">
                    <div class="stat-label">Pending</div>
                    <div class="stat-number">${pendingCount}</div>
                </div>
                <div class="stat-card stat-progress">
                    <div class="stat-label">In Progress</div>
                    <div class="stat-number">${progressCount}</div>
                </div>
                <div class="stat-card stat-completed">
                    <div class="stat-label">Completed</div>
                    <div class="stat-number">${completedCount}</div>
                </div>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h2 style="margin: 0;">Recent Activity</h2>
                <a href="${pageContext.request.contextPath}/tasks" class="btn btn-primary">
                    View All &rarrlp;
                </a>
            </div>

            <div class="card" style="padding: 0; overflow: hidden; margin-bottom: 1rem">
                <table class="table">
                    <thead>
                    <tr>
                        <th style="width: 10%;">ID</th>
                        <th style="width: 40%;">Title</th>
                        <th style="width: 25%;">Status</th>
                        <th style="width: 25%;">Created</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="task" items="${recentTasks}" varStatus="status">
                        <tr>
                            <td class="mono-text" style="color: var(--text-muted)">
                                #ID-${status.count}
                            </td>
                            <td style="font-weight: 500;">
                                <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}&vId=${status.count}" style="text-decoration: none; color: var(--text-main);" class="table-title-wrapper">
                                    <span class="text-truncate">${task.title}</span>
                                    <span class="edit-icon">✐</span>
                                </a>
                            </td>
                            <td>
                                <span class="badge badge-${task.taskStatus.name().toLowerCase()}">
                                    <c:choose>
                                        <c:when test="${task.taskStatus == 'IN_PROGRESS'}">
                                            In Progress
                                        </c:when>
                                        <c:otherwise>
                                            ${task.taskStatus}
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td style="font-size: 0.9rem; color: var(--text-muted);">
                                <c:choose>
                                    <c:when test="${task.createdAt != null}">
                                        <t:timeAgo date="${task.createdAt}" />
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentTasks}">
                        <tr>
                            <td colspan="4" style="text-align: center; padding: 5rem;">
                                You have no tasks yet :( <br>
                                <a href="${pageContext.request.contextPath}/tasks/new" style="color: var(--brand-orange);text-decoration: underline;">Create one now</a>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>