@echo off
echo ================================================
echo  Opening Windows Firewall for Spring Boot
echo  Port: 8080
echo ================================================
echo.
echo Right-click this file and select "Run as administrator"
echo.
pause

netsh advfirewall firewall delete rule name="Spring Boot Port 8080" >nul 2>&1
netsh advfirewall firewall add rule name="Spring Boot Port 8080" dir=in action=allow protocol=TCP localport=8080

if %errorlevel% equ 0 (
    echo.
    echo [SUCCESS] Firewall rule added successfully!
    echo Your Spring Boot API is now accessible from emulator/devices.
    echo.
) else (
    echo.
    echo [ERROR] Failed to add firewall rule.
    echo Please run this file as Administrator.
    echo.
)

pause
