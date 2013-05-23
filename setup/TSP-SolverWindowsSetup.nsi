# installer name
Name "TSP-Solver"
outFile "TSP-SolverWindowsSetup.exe"

# request administrator rights
RequestExecutionLevel admin

# install directory
InstallDir "$PROGRAMFILES\ZHAW\Softwareprojekt FS-13\TSP-Solver"


# ------------------------------------------------------------------------------
# default section start
section

# uninstall old version
ExecWait '"$INSTDIR\TSP-SolverUninstaller.exe" /S _?=$INSTDIR'


# output path
setOutPath $INSTDIR

# files to install
File /r "..\build\TSP-Solver.exe"
File /r "..\build\TSP-Solver.jar"

setOutPath $INSTDIR\data
File /r "..\data\*"

setOutPath $INSTDIR\lib
File /r "..\lib\*"


# write the uninstaller
writeUninstaller $INSTDIR\TSP-SolverUninstaller.exe


# create the start menu
SetShellVarContext all
setOutPath $INSTDIR
CreateDirectory "$SMPROGRAMS\ZHAW\Softwareprojekt FS-13\TSP-Solver"
CreateShortcut  "$SMPROGRAMS\ZHAW\Softwareprojekt FS-13\TSP-Solver\TSP-Solver.lnk" $INSTDIR\TSP-Solver.exe

# default section end
sectionEnd
# ------------------------------------------------------------------------------


# ------------------------------------------------------------------------------
# uninstaller section start
section "Uninstall"

# delete the install directory
RMDir /r $INSTDIR

# remove the start menu
SetShellVarContext all
Delete "$SMPROGRAMS\ZHAW\Softwareprojekt FS-13\TSP-Solver\TSP-Solver.lnk"
RMDir  "$SMPROGRAMS\ZHAW\Softwareprojekt FS-13\TSP-Solver"

# uninstaller section end
sectionEnd
# ------------------------------------------------------------------------------