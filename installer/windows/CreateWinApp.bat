@echo off
if exist output rmdir output /S /q
REM if exist apptemp rmdir apptemp /S /q
jpackage --verbose --type app-image --input input --dest output --name RuleGen --main-jar RuleGen.jar --main-class application.Main --icon input/PcPatrEditor.ico --module-path "C:\Users\Andy Black\Downloads\Java\AzulZulu\zulu17.30.15-ca-fx-jdk17.0.1-win_x64\jmods" --vendor "SIL International" --app-version 0.0.1
REM until have resources and documentation: call MoveResources.bat
