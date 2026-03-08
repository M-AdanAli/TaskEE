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

        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

        <div class="container">

            <div style="text-align: center; margin: 40px 0 60px 0;">
                <h1 style="font-size: 2.5rem;">Welcome back, <span class="hero-name">${userName}</span>!</h1>
                <p style="color: var(--text-muted); font-size: 1.3rem; letter-spacing: -0.02em;">
                    Here's your productivity overview...
                </p>
            </div>

            <div class="stats-grid">
                <a href="${pageContext.request.contextPath}/tasks" class="stat-card" style="text-decoration: none;">
                    <div class="stat-label">Total Tasks</div>
                    <div class="stat-number">${totalTasks}</div>
                </a>
                <a href="${pageContext.request.contextPath}/tasks?status=PENDING" class="stat-card stat-pending" style="text-decoration: none;">
                    <div class="stat-label">Pending</div>
                    <div class="stat-number">${pendingCount}</div>
                </a>
                <a href="${pageContext.request.contextPath}/tasks?status=IN_PROGRESS" class="stat-card stat-progress" style="text-decoration: none;">
                    <div class="stat-label">In Progress</div>
                    <div class="stat-number">${progressCount}</div>
                </a>
                <a href="${pageContext.request.contextPath}/tasks?status=COMPLETED" class="stat-card stat-completed" style="text-decoration: none;">
                    <div class="stat-label">Completed</div>
                    <div class="stat-number">${completedCount}</div>
                </a>
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
                        <th style="width: 30%;">Title</th>
                        <th style="width: 20%;">Status</th>
                        <th style="width: 20%;">Created</th>
                        <th style="width: 20%;">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="task" items="${recentTasks}" varStatus="status">
                        <tr>
                            <td class="mono-text" style="color: var(--text-muted)">
                                #ID-${status.count}
                            </td>
                            <td style="font-weight: 500;">
                                <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}&vId=${status.count}&source=dashboard" style="text-decoration: none; color: var(--text-main);" class="table-title-wrapper">
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
                            <td>
                                <c:if test="${task.taskStatus == 'PENDING'}">
                                    <form action="${pageContext.request.contextPath}/tasks/status" method="post" style="margin:0;">
                                        <input type="hidden" name="id" value="${task.id}">
                                        <input type="hidden" name="status" value="IN_PROGRESS">
                                        <button type="submit" class="btn btn-sm" style="background: #EBF8FF; color: #3182CE; border: 1px solid #90CDF4; font-weight: 600;" title="Mark In Progress">
                                             Start &#10095;
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${task.taskStatus == 'IN_PROGRESS'}">
                                    <form action="${pageContext.request.contextPath}/tasks/status" method="post" style="margin:0;">
                                        <input type="hidden" name="id" value="${task.id}">
                                        <input type="hidden" name="status" value="COMPLETED">
                                        <button type="submit" class="btn btn-sm" style="background: #F0FFF4; color: #38A169; border: 1px solid #9AE6B4; font-weight: 600;" title="Mark Completed">
                                              &#10003; Done
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${task.taskStatus == 'COMPLETED'}">
                                    <form action="${pageContext.request.contextPath}/tasks/delete" method="post" style="margin:0;">
                                        <input type="hidden" name="id" value="${task.id}">
                                        <!-- Ensure we stay on the tasks page after delete -->
                                        <input type="hidden" name="source" value="tasks">
                                        <button type="submit" class="btn btn-sm"
                                                style="background: #FFF5F5; color: #C53030; border: 1px solid #FEB2B2; font-weight: 600;"
                                                title="Delete Task"
                                                onclick="return confirm('Delete this completed task?');">
                                            &times; Delete
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentTasks}">
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 5rem;">
                                You have no tasks yet :( <br>
                                <a href="${pageContext.request.contextPath}/tasks/new?source=dashboard" style="color: var(--brand-orange);text-decoration: underline;">Create one now</a>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>