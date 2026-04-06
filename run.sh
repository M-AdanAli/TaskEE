#!/bin/bash

echo "==================================================="
echo "  TaskEE - Automated Build and Deploy"
echo "==================================================="

echo "[1/3] Building the WAR file and running tests..."
mvn clean install
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Build or Tests failed! Please check the logs above."
    exit 1
fi

echo ""
echo "[2/3] Preparing to open browser..."
# Run a background process that waits 5 seconds then opens the browser
(
    sleep 5
    if which xdg-open > /dev/null; then
        xdg-open http://localhost:8080/TaskEE
    elif which open > /dev/null; then
        open http://localhost:8080/TaskEE
    fi
) &

echo ""
echo "[3/3] Starting Apache Tomcat via Cargo..."
echo "(Press Ctrl+C to stop the server)"
mvn cargo:run