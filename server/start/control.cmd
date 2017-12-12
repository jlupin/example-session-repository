@echo off
TITLE JLupin Next Server Local Console
COLOR 0B
MODE CON: COLS=160 LINES=1500

if NOT exist "%JAVA_HOME%" (
    echo JAVA_HOME variable is not set. Set this variable and run again
	goto quit
)

if NOT exist "%JAVA_HOME%\bin\java.exe" (
    echo JAVA_HOME variable is not properly set. Set this variable and run again
	goto quit
)

if exist "%JAVA_HOME%\bin\java.exe" SET JLUPIN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

set SCRIPT_PATH=%~dp0

set ARGS=
:loop
if "%1"=="" goto continue
set ARGS=%ARGS% %1
shift
goto Loop
:continue

%JLUPIN_JAVA_EXE% -Dconsole.upload.dir=%SCRIPT_PATH%..\upload -Dcontrol.log_path=%SCRIPT_PATH%..\logs\control\control.log -Dcontrol.history_file_path=%SCRIPT_PATH%\control\control.history -Dcontrol.log_level=INFO -Dcontrol.address="127.0.0.1" -Dcontrol.port="9096" -Dcontrol.connectionTimeout="1000" -Dcontrol.readTimeout="60000" -jar %SCRIPT_PATH%control\control.jar %ARGS%

:quit
