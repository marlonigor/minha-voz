@echo off
title MinhaVoz - Inicializador do Emulador
echo ===================================================
echo     MinhaVoz - Inicializando Emulador Pixel 7
echo ===================================================
echo.
echo Iniciando o emulador Android Pixel 7...
start "" "C:\Users\omarl\AppData\Local\Android\Sdk\emulator\emulator.exe" -avd Pixel_7
echo.
echo Aguardando o boot do dispositivo...
"C:\Users\omarl\AppData\Local\Android\Sdk\platform-tools\adb.exe" wait-for-device
echo Instalando APK de debug atualizada...
"C:\Users\omarl\AppData\Local\Android\Sdk\platform-tools\adb.exe" install -r "%~dp0..\app\build\outputs\apk\debug\app-debug.apk"
echo Abrindo aplicativo MinhaVoz na tela...
"C:\Users\omarl\AppData\Local\Android\Sdk\platform-tools\adb.exe" shell am start -n br.com.minhavoz/.MainActivity
echo.
echo Aplicativo MinhaVoz pronto para teste!
pause
