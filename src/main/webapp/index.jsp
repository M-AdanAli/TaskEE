<%@ page import="com.adanali.taskee.dto.SessionUser" %>
<%
    SessionUser currentUser = (SessionUser) session.getAttribute("currentUser");

    if (currentUser != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>