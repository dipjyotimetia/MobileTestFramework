cd C:\Users\dipjyoti.metia\AppData\Roaming\npm\node_modules\appium\build\lib
@echo off & setlocal
node main.js -a 172.23.126.97 -p 4723 -cp 4723 -bp 4732"
if errorlevel 1 (
    echo Not successful
) else (
    echo Successful
)node