<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>My Tasks - TaskEE</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&family=JetBrains+Mono:wght@400&display=swap" rel="stylesheet">
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
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/tasks" class="active">My Tasks</a>
                <a href="${pageContext.request.contextPath}/profile">Profile</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-secondary" style="border: none; color: var(--text-main)">Logout</a>
            </div>
        </nav>

        <div class="container">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; margin-top: 2rem;">
                <div>
                    <h2 style="color: var(--primary-dark); margin: 0;">My Tasks</h2>
                    <p style="color: var(--text-muted);">Manage and track your work...</p>
                </div>
                <a href="${pageContext.request.contextPath}/tasks/new" class="btn btn-primary">
                    + Create New Task
                </a>
            </div>
            <div class="task-grid">
                <c:forEach var="task" items="${taskList}" varStatus="status">
                    <div class="task-card">
                        <div class="card-header">
                        <span class="badge badge-${task.taskStatus.name().toLowerCase()}">
                            <c:choose>
                                <c:when test="${task.taskStatus == 'IN_PROGRESS'}">In Progress</c:when>
                                <c:otherwise>${task.taskStatus}</c:otherwise>
                            </c:choose>
                        </span>
                            <span class="mono-text" style="color: #a0aec0; font-size: 0.85rem;">
                            #ID-${status.count}
                            </span>
                        </div>
                        <h3 class="card-title">
                            <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}&vId=${status.count}"
                               style="text-decoration: none; color: inherit;">
                                    ${task.title}
                            </a>
                        </h3>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty task.description}">
                                    <div style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">
                                            ${task.description}
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <em style="opacity: 0.6;">No description provided.</em>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="card-footer">
                            <div class="meta-date">
                                <span><strong>Created: </strong><t:timeAgo date="${task.createdAt}" /></span>

                                <c:if test="${not empty task.updatedAt}">
                                    <span><strong>Updated: </strong><t:timeAgo date="${task.updatedAt}" /></span>
                                </c:if>
                            </div>
                            <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}&vId=${status.count}"
                               class="btn btn-sm btn-secondary">
                                Open Details
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${empty taskList}">
                <div style="text-align: center; padding: 8rem; background: white; border: 1px solid var(--border-color);border-radius: var(--radius-md); box-shadow: var(--shadow-subtle); font-size: 0.95rem;">
                    <p>You have no tasks yet :(</p>
                    <a href="${pageContext.request.contextPath}/tasks/new" style="color: var(--brand-orange); text-decoration: underline;">Create one now</a>
                </div>
            </c:if>

        </div>

    </body>
</html>