<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>My Tasks | TaskEE</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&family=JetBrains+Mono:wght@400&display=swap" rel="stylesheet">
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

        <div class="container">
            <div class="d-flex flex-between align-center mt-4 mb-4">
                <div>
                    <h2 style="color: var(--primary-dark); margin: 0;">My Tasks</h2>
                    <p class="text-muted">Manage and track your work...</p>
                </div>
                <a href="${pageContext.request.contextPath}/tasks/new" class="btn btn-primary">
                    + Create New Task
                </a>
            </div>
            <div class="filter-bar">
                <a href="${pageContext.request.contextPath}/tasks"
                   class="filter-btn ${activeFilter == 'ALL' ? 'active' : ''}">
                    All Tasks
                </a>
                <a href="${pageContext.request.contextPath}/tasks?status=PENDING"
                   class="filter-btn ${activeFilter == 'PENDING' ? 'active' : ''}">
                    Pending
                </a>
                <a href="${pageContext.request.contextPath}/tasks?status=IN_PROGRESS"
                   class="filter-btn ${activeFilter == 'IN_PROGRESS' ? 'active' : ''}">
                    In Progress
                </a>
                <a href="${pageContext.request.contextPath}/tasks?status=COMPLETED"
                   class="filter-btn ${activeFilter == 'COMPLETED' ? 'active' : ''}">
                    Completed
                </a>
            </div>
            <div class="task-grid">
                <c:forEach var="task" items="${taskPage.items}" varStatus="status">
                    <div class="task-card">
                        <div class="card-header">
                        <span class="badge badge-${task.taskStatus.name().toLowerCase()}">
                            <c:choose>
                                <c:when test="${task.taskStatus == 'IN_PROGRESS'}">In Progress</c:when>
                                <c:otherwise>${task.taskStatus}</c:otherwise>
                            </c:choose>
                        </span>
                            <span class="mono-text text-muted" style="font-size: 0.85rem;">
                            #ID-${offset + status.count}
                            </span>
                        </div>
                        <h3 class="card-title">
                            <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}&vId=${status.count}" class="no-decoration">
                                    <c:out value='${task.title}' />
                            </a>
                        </h3>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty task.description}">
                                    <div style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">
                                        <c:out value='${task.description}' />
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
                            <div class="d-flex align-center gap-1">
                                <c:if test="${task.taskStatus == 'PENDING'}">
                                    <form action="${pageContext.request.contextPath}/tasks/status" method="post" style="margin:0;">
                                        <input type="hidden" name="id" value="${task.id}">
                                        <input type="hidden" name="status" value="IN_PROGRESS">
                                        <button type="submit" class="btn btn-sm font-bold" style="background: #EBF8FF; color: #3182CE; border: 1px solid #90CDF4;" title="Mark In Progress">
                                            Start &#10095;
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${task.taskStatus == 'IN_PROGRESS'}">
                                    <form action="${pageContext.request.contextPath}/tasks/status" method="post" style="margin:0;">
                                        <input type="hidden" name="id" value="${task.id}">
                                        <input type="hidden" name="status" value="COMPLETED">
                                        <button type="submit" class="btn btn-sm font-bold" style="background: #F0FFF4; color: #38A169; border: 1px solid #9AE6B4;" title="Mark Completed">
                                            &#10003; Done
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${task.taskStatus == 'COMPLETED'}">
                                    <form action="${pageContext.request.contextPath}/tasks/delete" method="post" style="margin:0;">
                                        <input type="hidden" name="id" value="${task.id}">
                                        <input type="hidden" name="source" value="tasks">
                                        <button type="submit" class="btn btn-sm font-bold"
                                                style="background: #FFF5F5; color: #C53030; border: 1px solid #FEB2B2;"
                                                title="Delete Task"
                                                onclick="return confirm('Delete this completed task?');">
                                            &times; Delete
                                        </button>
                                    </form>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}&vId=${status.count}&source=tasks"
                                   class="btn btn-sm btn-secondary">
                                    Edit Details
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${empty taskPage.items}">
                <div style="text-align: center; padding: 8rem; background: white; border: 1px solid var(--border-color);border-radius: var(--radius-md); box-shadow: var(--shadow-subtle); font-size: 0.95rem;">
                    <c:choose>
                        <c:when test="${not empty activeFilter && activeFilter != 'ALL'}">
                            <p>No '${activeFilter.replace('_', ' ')}' tasks found !</p>
                            <a href="${pageContext.request.contextPath}/tasks" style="color: var(--brand-orange); text-decoration: underline;">View All Tasks</a>
                        </c:when>
                        <c:otherwise>
                            <p>You have no tasks yet :(</p>
                            <a href="${pageContext.request.contextPath}/tasks/new" style="color: var(--brand-orange); text-decoration: underline;">Create one now</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <c:if test="${taskPage.totalPages > 1}">

                <!-- 1. Build the Base URL preserving the filter -->
                <c:set var="baseUrl" value="${pageContext.request.contextPath}/tasks?" />
                <c:if test="${activeFilter != 'ALL'}">
                    <c:set var="baseUrl" value="${baseUrl}status=${activeFilter}&" />
                </c:if>

                <c:set var="startPage" value="${taskPage.currentPage - 2}" />
                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>

                <c:set var="endPage" value="${startPage + 4}" />
                <c:if test="${endPage > taskPage.totalPages}">
                    <c:set var="endPage" value="${taskPage.totalPages}" />
                    <c:set var="startPage" value="${endPage - 4}" />
                    <c:if test="${startPage < 1}">
                        <c:set var="startPage" value="1" />
                    </c:if>
                </c:if>

                <div class="pagination">

                    <!-- Previous Button -->
                    <a href="${baseUrl}page=${taskPage.currentPage - 1}"
                       class="page-btn ${!taskPage.hasPrevious() ? 'disabled' : ''}">
                        &laquo;
                    </a>

                    <!-- First Page Indicator (If window moved past page 1) -->
                    <c:if test="${startPage > 1}">
                        <a href="${baseUrl}page=1" class="page-btn">1</a>
                        <span class="page-info">...</span>
                    </c:if>

                    <!-- Page Numbers (The Window) -->
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="${baseUrl}page=${i}"
                           class="page-btn ${taskPage.currentPage == i ? 'active' : ''}">
                                ${i}
                        </a>
                    </c:forEach>

                    <!-- Last Page Indicator (If window is before last page) -->
                    <c:if test="${endPage < taskPage.totalPages}">
                        <span class="page-info">...</span>
                        <a href="${baseUrl}page=${taskPage.totalPages}" class="page-btn">${taskPage.totalPages}</a>
                    </c:if>

                    <!-- Next Button -->
                    <a href="${baseUrl}page=${taskPage.currentPage + 1}"
                       class="page-btn ${!taskPage.hasNext() ? 'disabled' : ''}">
                        &raquo;
                    </a>

                </div>

            </c:if>

        </div>

    </body>
</html>