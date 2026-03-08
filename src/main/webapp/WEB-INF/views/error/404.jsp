<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Page Not Found | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body style="display: flex; align-items: center; justify-content: center; height: 100vh; text-align: center;">
        <div style="max-width: 500px; padding: 20px;">
            <h1 style="font-size: 6rem; color: var(--text-muted); margin: 0; line-height: 1;">404</h1>
            <h2 style="color: var(--primary-dark); margin: 20px 0;">Lost at Sea?</h2>
            <p style="color: var(--text-muted); margin-bottom: 30px;">
                The page you are looking for doesn't exist or has been moved.
            </p>

            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                Return to Dashboard
            </a>
        </div>

    </body>
</html>
