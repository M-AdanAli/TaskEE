<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${pageTitle} | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

        <div class="container" style="max-width: 700px; margin-top: 60px;">

            <div class="card" style="padding: 2.5rem; border-top: 5px solid var(--brand-orange);">

                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem;">
                    <div>
                        <h2 style="color: var(--primary-dark); margin: 0;">${pageTitle}</h2>
                    </div>
                    <c:if test="${not empty task}">
                        <div class="mono-text" style="background: #F7FAFC; padding: 5px 10px; border-radius: 4px; color: var(--text-muted); border: 1px solid var(--border-color);">
                            #<c:out value="${param.vId}" default="ID-${task.id}" />
                        </div>
                    </c:if>
                </div>

                <hr style="border: 0; border-top: 1px solid var(--border-color); margin-bottom: 2rem;">

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error mb-3">
                        <strong>Error:</strong> ${errorMessage}
                    </div>
                </c:if>

                <!-- MAIN FORM -->
                <form action="${pageContext.request.contextPath}/tasks/${not empty task ? 'update' : 'save'}" method="post">
                    <c:if test="${not empty task}">
                        <input type="hidden" name="id" value="${task.id}">
                    </c:if>
                    <input type="hidden" name="source" value="${not empty param.source ? param.source : 'tasks'}">
                    <div class="form-group">
                        <label class="mb-1">Title</label>
                        <input type="text" name="title" placeholder="What needs to be done?" value="${task.title}" maxlength="150" required autofocus>
                    </div>
                    <div class="form-group mt-2">
                        <label class="mb-1">Description</label>
                        <textarea name="description" rows="5"
                                  style="width: 100%; padding: 14px; border: 2px solid var(--border-color); border-radius: 6px; font-family: var(--font-body); font-size: 1rem; resize: none;"
                                  maxlength="5000">${task.description}</textarea>
                    </div>
                    <div class="form-group mb-3">
                        <label class="mb-1">Status</label>
                        <div class="status-selector">
                            <input type="radio" id="status-pending" name="status" value="PENDING"
                            ${(task.taskStatus == 'PENDING') || (empty task) ? 'checked' : ''}>
                            <label for="status-pending">Pending</label>
                            <input type="radio" id="status-in_progress" name="status" value="IN_PROGRESS"
                            ${task.taskStatus == 'IN_PROGRESS' ? 'checked' : ''}>
                            <label for="status-in_progress">In Progress</label>
                            <c:if test="${not empty task}">
                                <input type="radio" id="status-completed" name="status" value="COMPLETED"
                                    ${task.taskStatus == 'COMPLETED' ? 'checked' : ''}>
                                <label for="status-completed">Completed</label>
                            </c:if>
                        </div>
                    </div>

                    <div class="mt-5 pb-2 d-flex">
                        <button type="submit" class="btn btn-primary" style="padding: 12px 24px; font-size: 1rem;">
                            ${not empty task ? 'Save Changes' : 'Create Task'}
                        </button>
                        <a href="${pageContext.request.contextPath}/${not empty param.source && param.source == 'tasks'? 'tasks' : 'dashboard'}" class="btn btn-secondary" style="margin-left: 10px; padding: 12px 24px; font-size: 1rem;">
                            Cancel
                        </a>
                    </div>
                </form>

                <c:if test="${not empty task}">
                    <div class="form-meta">
                        <div style="font-size: 0.85rem; color: var(--text-muted); line-height: 1.4;">
                            <div class="d-flex align-center gap-1">
                                <span class="font-bold">Created:</span>
                                <t:timeAgo date="${task.createdAt}" />
                            </div>
                            <c:if test="${not empty task.updatedAt}">
                                <div class="d-flex align-center gap-1">
                                    <span class="font-bold">Updated:</span>
                                    <t:timeAgo date="${task.updatedAt}" />
                                </div>
                            </c:if>
                        </div>
                        <form action="${pageContext.request.contextPath}/tasks/delete" method="post">
                            <input type="hidden" name="id" value="${task.id}">
                            <input type="hidden" name="source" value="${not empty param.source ? param.source : 'tasks'}">
                            <button type="submit" class="btn btn-danger"
                                    style="font-size: 1rem; padding: 12px 24px"
                                    onclick="return confirm('Permanently delete this task?');">
                                Delete Task
                            </button>
                        </form>
                    </div>
                </c:if>

            </div>
        </div>

    </body>
</html>