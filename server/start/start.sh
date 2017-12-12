#!/bin/bash

##### FUNCTIONS #####
function Exit()
{
	exit 1
}

function Help()
{

	echo "Usage: start.sh EXEC_MODE LOG_LEVEL"
	echo "Usage: start.sh [-i|-b] [ERROR|WARN|INFO|DEBUG]"
	echo "Default: start.sh -b INFO"

}

function Header()
{
	echo "========================================================================="
	echo ""
	echo "  JLUPIN NEXT SERVER (mode: ${mode}, log level: ${logLevel})"
	echo ""
	echo "  JAVA_HOME: ${JAVA_HOME}"
	echo ""
	echo "  JAVA_OPTS: $JAVA_OPTS"
	echo ""
	echo "  JLUPIN_SERVER_HOME: ${JLUPIN_SERVER_HOME}"
	echo ""
	echo "  CLASSPATH_START: ${CLASSPATH_START}"
	echo ""
	echo "  JLUPIN_CLASSPATH: $JLUPIN_CLASSPATH"
	echo ""
	echo "  JAVA VERSION"
	"$JLUPIN_JAVA_EXE" -version
	echo ""
	echo "========================================================================="
	echo ""
	echo ${JLUPIN_SERVER_EXE}
	echo ""
}


##### SANITY CHECKS & SETUP BEFOR SERVER START #####

# Setting JLNS start mode & log level

modeSwitch=$1
logLevel=$2

# Default mode
[ "${modeSwitch}x" = "x" ] && modeSwitch="-b"

# Default Log Level
[ "${logLevel}x" = "x" ] && logLevel="INFO"

case ${modeSwitch} in
	"-b")
		mode="background"
	;;
	"-i")
		mode="interactive"

	;;

	*) 
		Help && Exit
	;;
esac

case $logLevel in
	"ERROR");;
	"WARN");;
	"INFO");;
	"DEBUG");;
	*) Help && Exit;;
esac

# Setting JLUPIN_SERVER_HOME 
REL_START_DIR=`dirname $0`
cd ${REL_START_DIR}
CURRENT_DIR=`pwd`
cd ..
JLUPIN_SERVER_HOME=`pwd`
cd ${CURRENT_DIR}

# Import setting from setenv
SETENV=${JLUPIN_SERVER_HOME}/start/configuration/setenv

[ -e ${SETENV} ] && source ${SETENV}


# Setting JAVA_HOME
if [ "${JAVA_HOME}x" = "x" ];
then
	# Trying to find JAVA_HOME
	if [ "`which java 2>/dev/null | grep -v 'alias java'`x" = "x" ];
	then
		echo "ERROR: I haven't found JAVA in the path"
		echo "INFO: If you have installed JDK in your system, please set JAVA_HOME properly (directory where JDK has been installed) or add JAVA bin directory to you PATH"
		echo "INFO: If you haven't installed JDK in your system yet, please visit http://www.oracle.com/technetwork/java/javase/downloads/index.html and follow the given instructions"
		Exit
	else
		[ `which java 2>/dev/null | wc -l` -gt 1 ] && echo "ERROR: Something confusing has happend, too many JAVA exec in your PATH. Please set one JAVA environment in your PATH and try again" && Exit
	
		JAVA_PATH=`which java 2>/dev/null | grep -v 'alias java'`

		if [ `echo ${JAVA_PATH} | grep -Ec "^~"` -lt 0 ];
		then
			 JAVA_FULL_PATH="${HOME}`echo ${JAVA_PATH} | sed s/"~"/""/g | sed s/"\/bin\/java"/""/g`" 
		else
			JAVA_FULL_PATH="`echo ${JAVA_PATH} | sed s/"\/bin\/java"/""/g`"
		fi

		export JAVA_HOME=${JAVA_FULL_PATH}
	fi	
fi


# Checking JAVA
JLUPIN_JAVA_EXE="${JAVA_HOME}/bin/java"

if [ ! -x "$JLUPIN_JAVA_EXE" ];
then
	echo "ERROR: JAVA exec does not exist or is not executable: $JLUPIN_JAVA_EXE"
	Exit
fi

# Server out logs
JLUPIN_SERVER_OUT_DIR="${JLUPIN_SERVER_HOME}/logs/server/main/start"
JLUPIN_SERVER_OUT="${JLUPIN_SERVER_OUT_DIR}/server.out"

if [ ! -e ${JLUPIN_SERVER_OUT_DIR} ]
then
        mkdir -p ${JLUPIN_SERVER_OUT_DIR}
        [ $? -ne 0 ] && echo "Cannot create SERVER_OUT directory (${JLUPIN_SERVER_OUT_DIR}), exiting..." && Exit
fi

# Classpath 
CLASSPATH_START="${JLUPIN_SERVER_HOME}/start/lib"
JLUPIN_CLASSPATH="$CLASSPATH_START/jlupin-starter-1.4.0.4.jar:$CLASSPATH_START/jlupin-util-1.4.0.4.jar:$CLASSPATH_START/jlupin-starter-logger-1.4.0.4.jar:$CLASSPATH_START/jlupin-reference-container-1.4.0.4.jar:$CLASSPATH_START/jlupin-interfaces-1.4.0.4.jar:$CLASSPATH_START/jlupin-classloader-1.4.0.4.jar:$CLASSPATH_START/jlupin-command-interpreter-1.4.0.4.jar:$CLASSPATH_START/jlupin-command-executor-1.4.0.4.jar:$CLASSPATH_START/jlupin-classloader-manager-1.4.0.4.jar:$CLASSPATH_START/jlupin-compilator-1.4.0.4.jar:$CLASSPATH_START/jlupin-singleton-container-1.4.0.4.jar:$CLASSPATH_START/jlupin-starter-logger-manager-1.4.0.4.jar:$CLASSPATH_START/jlupin-control-information-1.4.0.4.jar:$CLASSPATH_START/jlupin-printstream-strategy-manager-1.4.0.4.jar:$CLASSPATH_START/jlupin-common-1.4.0.4.jar:$CLASSPATH_START/jlupin-client-1.4.0.4.jar:$CLASSPATH_START/ext/snakeyaml-1.11.jar"

# Debug options
DEBUG_PARAMS=""
[ "$logLevel" = "DEBUG" ] && DEBUG_PARAMS="-agentlib:jdwp=transport=dt_socket,address=12998,server=y,suspend=n "


# Exec line
JLUPIN_SERVER_EXE="${JLUPIN_JAVA_EXE} ${JAVA_OPTS} ${DEBUG_PARAMS}-classpath ${JLUPIN_CLASSPATH} com.jlupin.starter.main.init.JLupinMainServerInitializer serverStart main.yml"



##### STARTING JLUPIN NEXT SERVER #####
if [ "${mode}" = "background" ]; then
	Header >>${JLUPIN_SERVER_OUT} 2>&1
	JLUPIN_SERVER_EXE="${JLUPIN_SERVER_EXE} consoleCommandModeOff startApplicationParallelModeOff main_server $logLevel >>${JLUPIN_SERVER_OUT} 2>&1 &"
else
	Header
	JLUPIN_SERVER_EXE="${JLUPIN_SERVER_EXE} consoleCommandModeOn startApplicationParallelModeOff main_server $logLevel"
fi

eval "${JLUPIN_SERVER_EXE}"
