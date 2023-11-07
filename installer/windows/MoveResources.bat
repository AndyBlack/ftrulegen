@echo off
REM Work-around to get exe to find resource and doc files
xcopy output\PcPatrEditor\app\resources output\PcPatrEditor\resources /E/s/i > nul
rmdir output\PcPatrEditor\app\resources /S /q > nul
xcopy output\PcPatrEditor\app\doc output\PcPatrEditor\doc /E/s/i > nul
rmdir output\PcPatrEditor\app\doc /S /q > nul
