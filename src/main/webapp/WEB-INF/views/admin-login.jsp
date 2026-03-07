<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">

        <style>
            /* Dark Mode Override */
            body { background-color: var(--text-main); color: #e2e8f0; }
            .login-card { background-color: var(--primary-dark); border: 1px solid #4a5568; border-top: 5px solid #F78C29; }
            label { color: #cbd5e0; }
            input { background-color: #1a202c; border-color: #4a5568; }
            input:focus { border-color: #F78C29; }
            .brand-logo { color: white; }
        </style>
    </head>
    <body>
        <div class="login-wrapper">
            <div class="brand-logo" style="display: flex; align-items: center; justify-content: center;">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                     alt="TaskEE Logo"
                     style="height: 100px; width: auto;">
                <p>TaskEE Admin</p>
            </div>

            <div class="card login-card">
                <h2 style="text-align: center; margin-bottom: 1.5rem; color: white;">System Access</h2>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error" style="background: #742a2a; border-color: #9b2c2c; color: #fc8181;">
                            ${errorMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin" method="post">
                    <div class="form-group">
                        <label>Administrator Email</label>
                        <input type="email" name="email" value="${param.email}" required autofocus>
                    </div>
                    <div class="form-group" style="margin-top: 1rem">
                        <label>Password</label>
                        <input type="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1.5rem;">Authenticate</button>
                </form>
            </div>
        </div>
    </body>
</html>