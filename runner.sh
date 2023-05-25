#!/bin/sh

echo "\n🐣 Github에서 프로젝트를 Pull 합니다.\n"

git pull
PORT=443
PROCESS_ID=$(sudo lsof -t -i:$PORT)

if [ -n "$PROCESS_ID" ]; then
        echo "\n🐣 구동중인 애플리케이션을 종료했습니다. (pid : $PROCESS_ID)\n"
        sudo kill  $PROCESS_ID
fi

echo "\n🐣 SpringBoot 프로젝트 빌드를 시작합니다.\n"

./gradlew bootJar -x test

echo "\n🐣 SpringBoot 애플리케이션을 실행합니다.\n"

sudo nohup java -jar ./build/libs/jwp-shopping-order.jar > spring.log &

exit
