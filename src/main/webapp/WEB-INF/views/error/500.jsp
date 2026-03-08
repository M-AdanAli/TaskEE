<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Internal Server Error | TaskEE</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    </head>
    <body style="display: flex; align-items: center; justify-content: center; height: 100vh; text-align: center;">
        <div style="max-width: 600px; padding: 20px;">
            <h1 style="font-size: 4rem; margin: 0;">😵</h1>
            <h2 style="color: var(--primary-dark); margin: 20px 0;">Something went wrong</h2>
            <p style="color: var(--text-muted); margin-bottom: 30px;">
                Our servers ran into an issue. The technical team has been notified.
            </p>

            <!-- Error Details (Hidden in Production usually, but good for Dev) -->
            <div style="background: white; padding: 15px; border: 1px solid #ddd; border-radius: 8px; text-align: left; margin-bottom: 30px; font-family: monospace; font-size: 0.85rem; color: var(--error-color); overflow: auto; max-height: 150px;">
                <strong>Error:</strong> ${pageContext.exception.message}
            </div>

            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                Reload Dashboard
            </a>
        </div>

    </body>
</html>
