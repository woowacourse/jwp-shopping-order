#!/bin/sh

echo 시작합니다~~
git pull origin main
./gradlew bootJar

fuser -k 8080/tcp

cd build/libs
nohup java -jar -Dspring.profiles.active=prod *.jar &
