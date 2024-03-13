To produce the Windows installer:

Make sure the program has been compiled.

In a command prompt with administrator privilege, make sure you have the path set to the correct version of Java and WiX.  (See setPath.bat)

Invoke BuildWin.bat

Note:

You will need to change the following as appropriate:

In CreateWinApp.bat, set the --app-version number and the --module-path.

In WinInstaller.bat, set the --app-version number and, if needed, copyright date.
