#!/bin/sh

BRANCH="step1"
REPOSITORY="/home/ubuntu"
PROJECT="jwp-shopping-order"

cd $REPOSITORY/$PROJECT

echo "> checkout $BRANCH"
git checkout $BRANCH

echo "> Git Pull"
git pull

echo "> 프로젝트 Build 시작"
./gradlew bootJar

CURRENT_PID=$(pgrep -f $PROJECT)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 2
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls $REPOSITORY/ | grep 'jwp-shopping-order' | tail -n 1)

cd $REPOSITORY/$PROJECT/build/libs
echo "> JAR Name: $JAR_NAME"

java -jar $JAR_NAME.jar &
