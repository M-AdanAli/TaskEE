<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body>
        <div class="login-wrapper">

            <div class="brand-logo d-flex align-center flex-center">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png"
                     alt="TaskEE Logo"
                     style="height: 100px; width: auto;">
                <p>TaskEE</p>
            </div>

            <div class="card login-card">

                <h2 class="text-center mb-3">Create Account !</h2>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">
                        <strong>Error:</strong> ${errorMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post">

                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" id="fullName" name="fullName" maxlength="100" value="<c:out value='${fullName}' />"
                               placeholder="Enter your Full Name" required autofocus>
                    </div>

                    <div class="form-group mt-2">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" maxlength="255" value="<c:out value='${email}' />"
                               placeholder="Enter your E-mail" required>
                    </div>

                    <div class="form-group mt-2">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password"
                               placeholder="Enter a Strong Password"
                               required
                               minlength="8"
                               maxlength="20"
                               pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[*!@#$%^&+=?()_\\-])(?=\S+$).{8,20}"
                               title="8-20 characters. Must contain 1 Uppercase, 1 Lowercase, 1 Number, and 1 Special Character">

                        <small style="color: var(--text-muted); font-size: 0.75rem; display: block; margin-top: 5px; line-height: 1.4;">
                            Must be 8-20 chars: 1 Uppercase, 1 Lowercase, 1 Number, 1 Special Character.
                        </small>
                    </div>

                    <div class="mt-3">
                        <button type="submit" class="btn btn-primary" style="width: 100%;">
                            Sign Up
                        </button>
                    </div>

                </form>

                <div class="text-center mt-3 text-muted" style="font-size: 0.9rem;">
                    Already have an account?
                    <a href="${pageContext.request.contextPath}/login" class="font-bold">Login</a>
                </div>
            </div>

            <div class="mt-3 text-muted" style="font-size: 0.8rem;">
                &copy; 2026 TaskEE Project
            </div>
        </div>

    </body>
</html>
