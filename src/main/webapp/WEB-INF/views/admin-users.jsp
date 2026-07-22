<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>User Management | TaskEE Admin</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&family=JetBrains+Mono:wght@400&display=swap" rel="stylesheet">
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

        <div class="container">

            <!-- Unified Admin Header -->
            <div class="admin-header">
                <h1 style="color: white; margin-bottom: 10px;">⚡ Admin Console</h1>
                <p style="color: #a0aec0; margin: 0;">Global platform control and monitoring.</p>
            </div>

            <!-- SUB-NAV TABS (User Management is Active) -->
            <div class="sub-nav">
                <a href="${pageContext.request.contextPath}/admin" class="sub-nav-tab">System Health</a>
                <a href="${pageContext.request.contextPath}/admin/users" class="sub-nav-tab active">User Management</a>
            </div>

            <!-- Flash Error -->
            <c:if test="${not empty sessionScope.flashError}">
                <div class="alert alert-error mb-3">
                    <strong>Error:</strong> <c:out value="${sessionScope.flashError}" />
                    <c:remove var="flashError" scope="session" />
                </div>
            </c:if>

            <div class="card" style="padding: 0; overflow: hidden;">
                <table class="table">
                    <thead>
                    <tr>
                        <th style="width: 5%;">ID</th>
                        <th style="width: 35%;">User</th>
                        <th style="width: 15%;">Role</th>
                        <th style="width: 15%;">Status</th>
                        <th style="width: 15%;">Joined</th>
                        <th style="width: 15%; text-align: right;">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="userSum" items="${userSummariesPage.items}" varStatus="loop">
                        <tr>
                            <td class="mono-text text-muted">#<c:out value="${userSum.id()}" /></td>
                            <td>
                                <div class="font-bold"><c:out value="${userSum.fullName()}" /></div>
                                <div class="text-muted" style="font-size: 0.85rem;"><c:out value="${userSum.email()}" /></div>
                            </td>
                            <td>
                                <span class="badge ${userSum.role().name() == 'ADMIN' ? 'badge-in_progress' : 'badge-pending'}">
                                    <c:out value="${userSum.role()}"/>
                                </span>
                            </td>
                            <td>
                                <span class="badge ${userSum.isActive() ? 'badge-completed' : 'badge-inactive'}">
                                        ${userSum.isActive() ? 'ACTIVE' : 'INACTIVE'}
                                </span>
                            </td>
                            <td class="text-muted" style="font-size: 0.9rem;">
                                <t:timeAgo date="${userSum.createdAt()}" />
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${userSum.id() == sessionScope.currentUser.id()}">
                                        <button class="btn btn-sm btn-secondary" disabled title="You cannot deactivate yourself" style="opacity: 0.5;">Current User</button>
                                    </c:when>
                                    <c:when test="${userSum.role().name() == 'ADMIN'}">
                                        <button class="btn btn-sm btn-secondary" disabled title="You cannot deactivate an Admin" style="opacity: 0.5;">Admin</button>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/admin/users/status" method="post" style="margin: 0;">
                                            <input type="hidden" name="id" value="<c:out value='${userSum.id()}'/>">
                                            <input type="hidden" name="status" value="<c:out value='${!userSum.isActive()}'/>">
                                            <input type="hidden" name="sourcePage" value="<c:out value='${userSummariesPage.currentPage}'/>">
                                            <c:choose>
                                                <c:when test="${userSum.isActive()}">
                                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Deactivate user? They will be kicked out immediately.');">Deactivate</button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="submit" class="btn btn-sm" style="background: #F0FFF4; color: #38A169; border: 1px solid #9AE6B4;">Activate</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty userSummariesPage.items}">
                        <tr><td colspan="6" class="text-center pb-2 text-muted">No users found.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <!-- PAGINATION CONTROLS -->
            <c:if test="${userSummariesPage.totalPages > 1}">
                <c:set var="baseUrl" value="${pageContext.request.contextPath}/admin/users?" />
                <c:set var="startPage" value="${userSummariesPage.currentPage - 2}" />
                <c:if test="${startPage < 1}"><c:set var="startPage" value="1" /></c:if>
                <c:set var="endPage" value="${startPage + 4}" />
                <c:if test="${endPage > userSummariesPage.totalPages}">
                    <c:set var="endPage" value="${userSummariesPage.totalPages}" />
                    <c:set var="startPage" value="${endPage - 4}" />
                    <c:if test="${startPage < 1}"><c:set var="startPage" value="1" /></c:if>
                </c:if>

                <div class="pagination">
                    <a href="${baseUrl}page=${userSummariesPage.currentPage - 1}" class="page-btn ${!userSummariesPage.hasPrevious() ? 'disabled' : ''}">&laquo;</a>
                    <c:if test="${startPage > 1}">
                        <a href="${baseUrl}page=1" class="page-btn">1</a><span class="page-info">...</span>
                    </c:if>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="${baseUrl}page=${i}" class="page-btn ${userSummariesPage.currentPage == i ? 'active' : ''}">${i}</a>
                    </c:forEach>
                    <c:if test="${endPage < userSummariesPage.totalPages}">
                        <span class="page-info">...</span><a href="${baseUrl}page=${userSummariesPage.totalPages}" class="page-btn">${userSummariesPage.totalPages}</a>
                    </c:if>
                    <a href="${baseUrl}page=${userSummariesPage.currentPage + 1}" class="page-btn ${!userSummariesPage.hasNext() ? 'disabled' : ''}">&raquo;</a>
                </div>

                <div class="text-center mt-2 text-muted" style="font-size: 0.85rem;">
                    Showing page ${userSummariesPage.currentPage} of ${userSummariesPage.totalPages} (${userSummariesPage.totalItems} total users)
                </div>
            </c:if>
        </div>
    </body>
</html>