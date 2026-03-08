<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Access Denied | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body style="display: flex; align-items: center; justify-content: center; height: 100vh; text-align: center;">
        <div style="max-width: 500px; padding: 20px;">
            <div style="font-size: 4rem;">🛑</div>
            <h2 style="color: var(--error-color); margin: 20px 0;">Access Denied</h2>
            <p style="color: var(--text-muted); margin-bottom: 30px;">
                You do not have permission to view this resource.
            </p>

            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">
                Go Back Home
            </a>
        </div>

    </body>
</html>
