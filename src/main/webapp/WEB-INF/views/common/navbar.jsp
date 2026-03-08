<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar">
    <div class="nav-brand">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png"
             alt="TaskEE Logo"
             style="height: 50px; width: auto;">
        <span>TaskEE</span>
    </div>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/dashboard" class="${activePage == 'dashboard' ? 'active' : ''}">Dashboard</a>
        <a href="${pageContext.request.contextPath}/tasks" class="${activePage == 'tasks' ? 'active' : ''}">My Tasks</a>
        <a href="${pageContext.request.contextPath}/profile" class="${activePage == 'profile' ? 'active' : ''}">Profile</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-secondary" style="border: none; color: var(--text-main)">Logout</a>
    </div>
</nav>
