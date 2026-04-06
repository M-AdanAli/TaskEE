@echo off
echo ===================================================
echo   TaskEE - Automated Build and Deploy
echo ===================================================

echo [1/3] Building the WAR file and running tests...
call mvn clean install
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Build or Tests failed! Please check the logs above.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [2/3] Opening browser to http://localhost:8080/TaskEE ...
:: Wait 10 seconds to let Tomcat start before opening the browser
start "" cmd /c "timeout /t 10 >nul & start http://localhost:8080/TaskEE"

echo.
echo [3/3] Starting Apache Tomcat via Cargo...
echo (Press Ctrl+C to stop the server)
call mvn cargo:run