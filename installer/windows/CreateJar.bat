@echo off
cd ..\..\mods
REM jar cmf META-INF\MANIFEST.MF RuleGen.jar .
jar cf RuleGen.jar .
copy RuleGen.jar ..\installer\windows\input > nul
del RuleGen.jar > nul
cd ..\installer\windows
