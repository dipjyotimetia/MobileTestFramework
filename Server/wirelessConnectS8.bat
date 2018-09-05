cd C:\Users\dipjyoti.metia\AppData\Local\Android\Sdk\platform-tools
@echo off & setlocal
set IP=172.23.25.185:5555
adb connect %IP% | find /i "connected to" >nul
if errorlevel 1 (
    echo Not successful
) else (
    echo Successful
)adb