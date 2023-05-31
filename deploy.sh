#!/bin/sh
PROJECT_NAME=jwp-shopping-order

echo '> 현재 구동중인 애플리케이션 pid 확인'
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo ''
if [ -z "$CURRENT_PID" ]; then
  echo '> 현재 구동 중인 애플리케이션이 없으므로 종료하지 >    않습니다.'
else
  echo '> 구동 중인 애플리케이션을 종료합니다. PID :' $CURRENT_PID
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

sudo chmod +x $PROJECT_NAME.jar

echo ''
echo '애플리케이션을 실행합니다.'
sudo nohup java -jar -Dspring.config.location=env.properties \
  -Dspring.profiles.active=prod \
  $PROJECT_NAME.jar > ~/application.log 2>&1 &
