@echo off
setlocal
cd /d "%~dp0\.."

echo Checking environment prerequisites...
where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Java runtime not found in PATH.
    exit /b 1
)

echo Pre-warming Gradle daemon and dependencies...
call gradlew.bat --version
if %ERRORLEVEL% neq 0 (
    echo Error: Gradle wrapper execution failed.
    exit /b %ERRORLEVEL%
)

echo Setup completed successfully.
exit /b 0
