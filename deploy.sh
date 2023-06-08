#!/bin/sh
PROJECT_NAME=jwp-shopping-order

echo '> 현재 구동중인 애플리케이션 pid 확인'
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo ''
if [ -z "$CURRENT_PID" ]; then
        echo '> 현재 구동 중인 애플리케이션 없음'
else
        echo '> kill -15 $CURRENT_PID'
                sudo kill -15 $CURRENT_PID
        sleep 5
fi

echo ''
sudo chmod +x $PROJECT_NAME.jar
sudo nohup java -jar \
-Dspring.profiles.active=prod \
-Dspring.config.import=env.properties \
$PROJECT_NAME.jar > ~/application.log 2>&1 &
