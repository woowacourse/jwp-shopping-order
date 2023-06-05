#!/bin/bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
APPNAME="jwp-shopping-order"
APPDIR=${ABSDIR}/${APPNAME}

echo "구동중인 애플리케이션을 확인합니다."

CURRENT_PID=$(pgrep -f ${APPNAME}.jar)

if [ ! -z ${CURRENT_PID} ]; then
        echo "기존 애플리케이션이 실행중이므로 종료합니다."
        kill -15 ${CURRENT_PID}
        sleep 5
fi

echo "애플리케이션을 실행합니다."

nohup java -jar ${ABSDIR}/${APPNAME}.jar --spring.profiles.active=prod 1>> build.log 2>> build_error.log &
