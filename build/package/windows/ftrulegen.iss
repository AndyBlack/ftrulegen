;This file will be executed next to the application bundle image
;I.e. current directory will contain folder FLExTrans Rule Assistant with application files
[Setup]
AppId={{76BEFB7D-1F55-46E9-88B7-B6DFAA8CF97C}
AppName=FLExTransRuleAssistant
AppVersion=0.28.1
AppVerName=FLExTransRuleAssistant version 0.28.1
AppPublisher=SIL International
AppComments=FLExTransRuleAssistant
AppCopyright=Copyright © 2023-2024 SIL International
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={pf}\SIL\FLExTransRuleAssistant
DisableStartupPrompt=Yes
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=No
DisableFinishedPage=No
DisableWelcomePage=No
DefaultGroupName=SIL International
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1
OutputBaseFilename=FLExTransRuleAssistant-0.28.1
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
SetupIconFile=package\windows\FLExTransRuleAssistant.ico
			
UninstallDisplayIcon={app}\FLExTransRuleAssistant.ico
UninstallDisplayName=FLExTransRuleAssistant version 0.28.1
WizardImageStretch=No
WizardSmallImageFile=package\windows\FLExTransRuleAssistant-setup-icon.bmp
ArchitecturesInstallIn64BitMode=x64
ChangesAssociations=yes

[Registry]

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "FLExTransRuleAssistant.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "FLExTransRuleAssistant\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\FLExTransRuleAssistant"; Filename: "{app}\FLExTransRuleAssistant.exe"; IconFilename: "{app}\FLExTransRuleAssistant.ico"; Check: returnTrue()



[Run]
Filename: "{app}\FLExTransRuleAssistant.exe"; Parameters: "-Xappcds:generatecache -Djavax.xml.accessExternalDTD=all"; Check: returnFalse()
Filename: "{app}\FLExTransRuleAssistant.exe"; Description: "{cm:LaunchProgram,FLExTransRuleAssistant}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\FLExTransRuleAssistant.exe"; Parameters: "-install -svcName ""FLExTransRuleAssistant"" -svcDesc ""A tool to make it easier to write FLExTrans transfer rules."" -mainExe ""FLExTransRuleAssistant.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\FLExTransRuleAssistant.exe "; Parameters: "-uninstall -svcName FLExTransRuleAssistant -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support?
  Result := True;
end;
	