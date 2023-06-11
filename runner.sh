#!/bin/bash

echo "\n🐣 Github에서 프로젝트를 Pull 합니다.\n"

git pull

JAR_NAME="jwp-shopping-order.jar"
PROCESS_ID=$(pgrep -f "$JAR_NAME")

if [ -n "$PROCESS_ID" ]; then
  sudo kill $PROCESS_ID
  echo "\n🐣 구동중인 애플리케이션을 종료했습니다. (pid : $PROCESS_ID)\n"
fi

echo "\n🐣 SpringBoot 프로젝트 빌드를 시작합니다.\n"

./gradlew clean bootJar

echo "\n🐣 SpringBoot 애플리케이션을 실행합니다.\n"

nohup java -jar ./build/libs/$JAR_NAME --server.port=8086 >spring.log &

exit
