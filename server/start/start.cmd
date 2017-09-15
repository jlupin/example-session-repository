@echo off

SET LOG_LEVEL=%1

if "%LOG_LEVEL%" == "" (
	SET LOG_LEVEL=INFO
)

IF "%LOG_LEVEL%" == "/?" (
	goto help
)

IF NOT "%LOG_LEVEL%" == "ERROR" (
	IF NOT "%LOG_LEVEL%" == "WARN" (
		IF NOT "%LOG_LEVEL%" == "INFO" (
			IF NOT "%LOG_LEVEL%" == "DEBUG" (
				goto help
			)
				
		)
	)
)


cd /d %~dp0
SET CURRENT_PATH=%~dp0

SET CONFIG_SETENV=%CURRENT_PATH%configuration\setenv

if NOT exist "%CONFIG_SETENV%" (
    echo %SCONFIG_SETENV% does not exist. Check the environment and run again.
	goto quit
)

SET JAVA_OPTS=
for /f "delims=" %%x in ('findstr /v "^#" %CONFIG_SETENV%') do (SET "%%x")
SET JAVA_OPTS=%JAVA_OPTS:"=%

if exist "%JAVA_HOME%" (
	SET JAVA_HOME=%JAVA_HOME:"=%
)

if NOT exist "%JAVA_HOME%" (
    echo JAVA_HOME variable is not set. Set this variable and run again.
	goto quit
)

if NOT exist "%JAVA_HOME%\bin\java.exe" (
    echo JAVA_HOME variable is not properly set. Set this variable and run again
	goto quit
)

if exist "%JAVA_HOME%\bin\java.exe" SET JLUPIN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"


SET CLASSPATH_START=%CURRENT_PATH%lib

SET JLUPIN_CLASSPATH=%CLASSPATH_START%\jlupin-starter-1.4.0.3.jar;%CLASSPATH_START%\jlupin-util-1.4.0.3.jar;%CLASSPATH_START%\jlupin-starter-logger-1.4.0.3.jar;%CLASSPATH_START%\jlupin-reference-container-1.4.0.3.jar;%CLASSPATH_START%\jlupin-interfaces-1.4.0.3.jar;%CLASSPATH_START%\jlupin-classloader-1.4.0.3.jar;%CLASSPATH_START%\jlupin-command-interpreter-1.4.0.3.jar;%CLASSPATH_START%\jlupin-command-executor-1.4.0.3.jar;%CLASSPATH_START%\jlupin-classloader-manager-1.4.0.3.jar;%CLASSPATH_START%\jlupin-compilator-1.4.0.3.jar;%CLASSPATH_START%\jlupin-singleton-container-1.4.0.3.jar;%CLASSPATH_START%\jlupin-starter-logger-manager-1.4.0.3.jar;%CLASSPATH_START%\jlupin-control-information-1.4.0.3.jar;%CLASSPATH_START%\jlupin-common-1.4.0.3.jar;%CLASSPATH_START%\jlupin-printstream-strategy-manager-1.4.0.3.jar;%CLASSPATH_START%\jlupin-client-1.4.0.3.jar;%CLASSPATH_START%\ext\snakeyaml-1.11.jar

echo '
echo '========================================================================='
echo '
echo JLUPIN NEXT SERVER
echo '
echo JAVA_HOME directory is %JAVA_HOME%
echo '
echo JAVA_OPTS: %JAVA_OPTS%
echo '
echo CURRENT_PATH: %CURRENT_PATH%
echo '
echo JLUPIN_CLASSPATH: %JLUPIN_CLASSPATH%
echo '
echo jvm info:
%JLUPIN_JAVA_EXE% -version
echo '
echo '========================================================================='
echo '

IF "%LOG_LEVEL%" == "DEBUG" (
	SET DEBUG_PARAMS=-agentlib:jdwp=transport=dt_socket,address=12998,server=y,suspend=n
	%JLUPIN_JAVA_EXE% %JAVA_OPTS% %DEBUG_PARAMS% -cp %JLUPIN_CLASSPATH% com.jlupin.starter.main.init.JLupinMainServerInitializer serverStart main.yml consoleCommandModeOn startApplicationParallelModeOff main_server %LOG_LEVEL%
) ELSE (
	%JLUPIN_JAVA_EXE% %JAVA_OPTS% -cp %JLUPIN_CLASSPATH% com.jlupin.starter.main.init.JLupinMainServerInitializer serverStart main.yml consoleCommandModeOn startApplicationParallelModeOff main_server %LOG_LEVEL%
)

goto quit

:help
echo Usage: start.cmd LOG_LEVEL
echo Usage: start.cmd [ERROR,WARN,INFO,DEBUG]
echo Default: start.cmd INFO

:quit