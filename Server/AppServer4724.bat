cd C:\Users\dipjyoti.metia\AppData\Roaming\npm\node_modules\appium\build\lib
@echo off & setlocal
node main.js -a 172.23.126.97 -p 4724 -cp 4724 -bp 4742"
if errorlevel 1 (
    echo Not successful
) else (
    echo Successful
)node