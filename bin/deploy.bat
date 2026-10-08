@echo off
setlocal
cd /d "%~dp0\.."

echo Checking environment prerequisites...
where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Java runtime not found in PATH.
    exit /b 1
)

echo Building release APK and App Bundle artifacts...
call gradlew.bat assembleRelease bundleRelease --stacktrace
if %ERRORLEVEL% neq 0 (
    echo Error: Release build failed.
    exit /b %ERRORLEVEL%
)

echo Release build completed successfully.
echo Artifacts available in app/build/outputs/apk/release/ and app/build/outputs/bundle/release/.
exit /b 0
