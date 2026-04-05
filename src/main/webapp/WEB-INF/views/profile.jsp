<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>My Profile | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

        <div class="container mt-5" style="max-width: 600px;">

            <h2 style="color: var(--primary-dark); margin-bottom: 30px;">Account Settings</h2>

            <div class="card" style="margin-bottom: 3rem; border-top: 5px solid var(--brand-orange);">
                <h3 class="mt-3">Profile Details</h3>

                <c:if test="${not empty successProfile}">
                    <div class="alert alert-success">${successProfile}</div>
                </c:if>
                <c:if test="${not empty errorProfile}">
                    <div class="alert alert-error">${errorProfile}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/profile/update" method="post">
                    <div class="form-group">
                        <label>Email Address</label>
                        <input type="email" value="<c:out value='${sessionScope.currentUser.email()}' />" disabled
                               style="background-color: #f7fafc; color: var(--text-muted); cursor: not-allowed;">
                        <small class="text-muted">Email cannot be changed.</small>
                    </div>
                    <div class="form-group mt-2">
                        <label>Full Name</label>
                        <input type="text" name="fullName" value="<c:out value='${sessionScope.currentUser.fullName()}' />" required maxlength="100">
                    </div>
                    <button type="submit" class="btn btn-primary mt-3">Update Profile</button>
                </form>
            </div>
            <div class="card" style="border-top: 5px solid var(--error-color);">
                <h3 class="mt-3">Security</h3>

                <c:if test="${not empty successPassword}">
                    <div class="alert alert-success">${successPassword}</div>
                </c:if>
                <c:if test="${not empty errorPassword}">
                    <div class="alert alert-error">${errorPassword}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/profile/password" method="post">
                    <div class="form-group">
                        <label>Current Password</label>
                        <input type="password" name="currentPassword" placeholder="Enter your Current Password" required>
                    </div>

                    <div class="form-group mt-2">
                        <label>New Password</label>
                        <input type="password" id="newPassword" name="newPassword"
                               placeholder="Enter a New Password"
                               required
                               minlength="8"
                               maxlength="20"
                               pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[*!@#$%^&+=?()_\\-])(?=\S+$).{8,20}"
                               title="8-20 characters. Must contain 1 Uppercase, 1 Lowercase, 1 Number, and 1 Special Character">

                        <small style="color: var(--text-muted); font-size: 0.75rem; display: block; margin-top: 5px; line-height: 1.4;">
                            Must be 8-20 chars: 1 Uppercase, 1 Lowercase, 1 Number, 1 Special Character.
                        </small>
                    </div>

                    <button type="submit" class="btn btn-secondary btn-danger mt-3">Change Password</button>
                </form>
            </div>

        </div>
    </body>
</html>