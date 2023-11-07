@echo off
if exist installtemp rmdir installtemp /S /q
jpackage --type exe --copyright "2023 SIL International" --description "FLExTRans Rule Generator" --name RuleGen --install-dir "SIL\RuleGen" --resource-dir input/resources --app-image output/RuleGen --win-menu --win-shortcut --license-file License.txt --temp installtemp --app-version 0.0.1 --vendor "SIL International"

