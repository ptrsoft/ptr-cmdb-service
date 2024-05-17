#!/usr/bin/env bash

CMD=$1
NOHUP=${NOHUP:=$(which nohup)}
PS=${PS:=$(which ps)}

# default java
JAVA_CMD=${JAVA_CMD:=$(which java)}

get_pid() {
    cat "${SERVICE_PID}" 2> /dev/null
}

pid_running() {
    kill -0 $1 2> /dev/null
}

die() {
    echo $*
    exit 1
}

if [ -n "$JAVA_HOME" ]
then
    # try to use $JAVA_HOME
    if [ -x "$JAVA_HOME"/bin/java ]
    then
        JAVA_CMD="$JAVA_HOME"/bin/java
    else
        die "$JAVA_HOME"/bin/java is not executable
    fi
fi


# take variables from environment if set
SERVICE_NAME="appkube-cmdb-service"
SERVICE_JAR=${SERVICE_JAR:=core-service-impl/target/core-service-impl-0.0.1-SNAPSHOT.jar}
SERVICE_PID=${SERVICE_PID:=service-pid.pid}
LOG_FILE=${LOG_FILE:=console.log}
#LOG4J=${LOG4J:=}
DEFAULT_JAVA_OPTS="-Djdk.io.File.enableADS=true -Dserver.profile=$2 -Ddb.server.host=$3 -Ddb.server.port=$4 -Ddb.schema=$5 -Ddb.user=$6 -Ddb.password=$7 -Djdk.tls.acknowledgeCloseNotify=true -Xms1g -Xmx1g -XX:NewRatio=1 -XX:+ResizeTLAB -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:+CMSClassUnloadingEnabled -XX:-OmitStackTraceInFastThrow"

if $JAVA_CMD -XX:+PrintFlagsFinal 2>&1 |grep -q UseParNewGC; then
	DEFAULT_JAVA_OPTS="${DEFAULT_JAVA_OPTS} -XX:+UseParNewGC"
fi

JAVA_OPTS="${JAVA_OPTS:="$DEFAULT_JAVA_OPTS"}"

start() {
  echo "Starting ${SERVICE_NAME} ...${JAVA_CMD}"
  "${NOHUP}" "${JAVA_CMD}" ${JAVA_OPTS} -jar "${SERVICE_JAR}"   >> "${LOG_FILE}" 2>> "${LOG_FILE}" & echo $! > "${SERVICE_PID}"
}

run() {
    echo "Running ${SERVICE_NAME} ..."
    exec "${JAVA_CMD}" ${JAVA_OPTS} -jar "${SERVICE_JAR}"  >> "${LOG_FILE}" 2>> "${LOG_FILE}" & echo $! > "${SERVICE_PID}"
}

stop() {
    if [ ! -f "${SERVICE_PID}" ]; then
      die "Not stopping. PID file not found: ${SERVICE_PID}"
    fi

    PID=$(get_pid)

    echo "Stopping ${SERVICE_NAME}. ($PID) ..."
    echo "Waiting for ${SERVICE_NAME} to halt..."

    kill $PID

    while "$PS" -p $PID > /dev/null; do sleep 1; done;
    rm -f "${SERVICE_PID}"

    echo "${SERVICE_NAME} stopped"
}

restart() {
    echo "Restarting ${SERVICE_NAME} ..."
    stop
    start
}

status() {
    PID=$(get_pid)
    if [ ! -z $PID ]; then
        if pid_running $PID; then
            echo "${SERVICE_NAME} running with PID ${PID}"
            return 0
        else
            rm "${SERVICE_PID}"
            die "Removed stale PID file ${SERVICE_PID} with ${PID}."
        fi
    fi

    die "${SERVICE_NAME} not running"
}

case "$CMD" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    run)
        run
        ;;
    *)
        echo "Usage $0 {start|stop|restart|status|run}"
esac

