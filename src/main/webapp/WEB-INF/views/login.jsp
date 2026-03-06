<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body>
        <div class="login-wrapper">

            <div class="brand-logo" style="display: flex; align-items: center; justify-content: center;">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                     alt="TaskEE Logo"
                     style="height: 100px; width: auto;">
                <p>TaskEE</p>
            </div>

            <div class="card login-card">

                <h2 style="text-align: center; margin-bottom: 1.5rem;">Welcome Back !</h2>

                <c:if test="${param.logout}">
                    <div class="alert alert-success" style="text-align: center;">
                        You have been logged out successfully.
                    </div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">
                        <strong>Error:</strong> ${errorMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">

                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" value="${email}"
                               placeholder="Enter your E-mail" required autofocus>
                    </div>

                    <div class="form-group" style="margin-top: 1rem">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password"
                               placeholder="Enter your Password" required>
                    </div>

                    <div style="margin-top: 1.5rem;">
                        <button type="submit" class="btn btn-primary" style="width: 100%;">
                            Sign In
                        </button>
                    </div>

                </form>

                <div style="text-align: center; margin-top: 1.5rem; color: var(--text-muted); font-size: 0.9rem;">
                    Don't have an account?
                    <a href="${pageContext.request.contextPath}/register" style="font-weight: 600;">Register</a>
                </div>

            </div>

            <div style="margin-top: 20px; color: var(--text-muted); font-size: 0.8rem;">
                &copy; 2026 TaskEE Project
            </div>

        </div>
    </body>
</html>
