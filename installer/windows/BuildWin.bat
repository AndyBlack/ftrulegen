@echo off
REM Build windows installer; see ReadmeWinInstaller.txt
echo PrepInput
call PrepInput.bat
echo CreateWinApp
call CreateWinApp.bat
echo WinInstaller
call WinInstaller.bat
rundll32 user32.dll,MessageBeep
