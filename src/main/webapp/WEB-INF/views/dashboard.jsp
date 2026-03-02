<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-secondary" style="border: none;">Logout</a>
            </div>
        </nav>

        <div class="container">

            <div style="text-align: center; margin: 40px 0 60px 0;">
                <h1 style="font-size: 2.5rem; margin-bottom: 3px;">Welcome back, ${userName}!</h1>
                <p style="color: var(--text-muted); font-size: 1.3rem; letter-spacing: -0.02em;">
                    What's in your mind for today?
                </p>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h2 style="margin: 0;">My Tasks</h2>
                <a href="${pageContext.request.contextPath}/tasks/new" class="btn btn-primary">
                    + New Task
                </a>
            </div>

            <div class="card" style="padding: 0; overflow: hidden;">

                <table class="table">
                    <thead>
                        <tr>
                            <th style="width: 5%;">ID</th> <!-- Visual Index -->
                            <th style="width: 35%;">Title</th>
                            <th style="width: 20%;">Status</th>
                            <th style="width: 20%;">Created At</th>
                            <th style="width: 20%; text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="task" items="${taskList}" varStatus="status">
                        <tr>
                            // Currently generating dynamic task IDs on the user end for better UX.
                            <td class="mono-text" style="color: var(--text-muted);">
                                    #${status.count}
                            </td>

                            <td style="font-weight: 500;">${task.title}</td>

                            <td>
                                <span class="badge badge-${task.status.name().toLowerCase()}">
                                        ${task.status}
                                </span>
                            </td>

                            <td style="font-size: 0.9rem; color: var(--text-muted);">
                                <c:choose>
                                    <c:when test="${task.createdAt != null}">
                                        ${task.createdAt.toLocalDate()}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>

                            <td style="text-align: right;">
                                <!-- The Real ID is used in the URL for logic -->
                                <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}"
                                   class="btn btn-sm btn-secondary">Edit</a>

                                <form action="${pageContext.request.contextPath}/tasks/delete" method="post" style="display: inline;">
                                    <input type="hidden" name="id" value="${task.id}">
                                    <button type="submit" class="btn btn-sm btn-danger"
                                            style="margin-left: 5px;"
                                            onclick="return confirm('Delete task #${status.count}?');">
                                        &times;
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Empty State -->
                    <c:if test="${empty taskList}">
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 4rem; color: var(--text-muted);">
                                You have no tasks yet. <br>
                                <a href="${pageContext.request.contextPath}/tasks/new" style="color: var(--brand-orange);">Create one now</a>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>