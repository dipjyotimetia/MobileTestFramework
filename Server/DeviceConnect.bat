cd C:\Users\dipjyoti.metia\AppData\Local\Android\Sdk\platform-tools
@echo off & setlocal
adb -s 9889d6324131325a34 tcpip 5555
if errorlevel 1 (
    echo Not successful
) else (
    echo Successful
)adb