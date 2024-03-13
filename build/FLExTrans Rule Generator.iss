;This file will be executed next to the application bundle image
;I.e. current directory will contain folder FLExTrans Rule Generator with application files
[Setup]
AppId={{0D1B3C45-644B-4D49-9DF6-C80217810A1B}
AppName=FLExTrans Rule Generator
AppVersion=0.1.0
AppVerName=FLExTrans Rule Generator 0.1.0
AppPublisher=SIL International
AppComments=RuleGen
AppCopyright=Copyright © 2023 SIL International
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={pf}\FLExTrans Rule Generator
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=SIL International
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=FLExTrans Rule Generator-0.1.0
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
SetupIconFile=package\windows\FLExTransWindowIcon.ico
UninstallDisplayIcon={app}\FLExTrans Rule Generator.ico
UninstallDisplayName=FLExTrans Rule Generator
WizardImageStretch=No
WizardSmallImageFile=package\windows\FLExTrans Rule Generator-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "FLExTrans Rule Generator\FLExTrans Rule Generator.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "FLExTrans Rule Generator\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\FLExTrans Rule Generator"; Filename: "{app}\FLExTrans Rule Generator.exe"; IconFilename: "{app}\FLExTrans Rule Generator.ico"; Check: returnTrue()
Name: "{commondesktop}\FLExTrans Rule Generator"; Filename: "{app}\FLExTrans Rule Generator.exe";  IconFilename: "{app}\FLExTrans Rule Generator.ico"; Check: returnTrue()


[Run]
Filename: "{app}\FLExTrans Rule Generator.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\FLExTrans Rule Generator.exe"; Description: "{cm:LaunchProgram,FLExTrans Rule Generator}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\FLExTrans Rule Generator.exe"; Parameters: "-install -svcName ""FLExTrans Rule Generator"" -svcDesc ""RuleGen is a tool that makes it easier to write FLExTrans transfer rules."" -mainExe ""FLExTrans Rule Generator.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\FLExTrans Rule Generator.exe "; Parameters: "-uninstall -svcName FLExTrans Rule Generator -stopOnUninstall"; Check: returnFalse()

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
