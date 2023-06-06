#!/bin/bash

REPOSITORY=/home/ubuntu
PROJECT_NAME=jwp-shopping-order
BUILD_DIR=build/libs

echo "> cd $REPOSITORY/$PROJECT_NAME/"
cd $REPOSITORY/$PROJECT_NAME/

echo "> git pull"
git pull

echo "> build start"
./gradlew bootJar

echo "> cd $REPOSITORY/$PROJECT_NAME/$BUILD_DIR/"
cd $REPOSITORY/$PROJECT_NAME/$BUILD_DIR/

echo "> check running application"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)
if [ -z "$CURRENT_PID" ]; then
  echo "> nothing exist"
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 "$CURRENT_PID"
  echo "> sleep until shutdown"
  while [ $(pgrep -f ${PROJECT_NAME}.*.jar) ]
  do
    echo "> zzz"
    sleep 1
  done
fi

echo "> start server"
java -jar $PROJECT_NAME.jar &
