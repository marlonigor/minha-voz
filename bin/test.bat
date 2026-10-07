@echo off
setlocal
cd /d "%~dp0\.."

echo Running test suite and lint...
call gradlew.bat test lintDebug --stacktrace
if %ERRORLEVEL% neq 0 (
    echo Tests or lint failed.
    exit /b %ERRORLEVEL%
)

echo All checks passed successfully.
exit /b 0
