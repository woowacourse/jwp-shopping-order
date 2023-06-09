#!/bin/bash

BRANCH_NAME=${1}

# git pull -> 특정 브랜치에서 가져오기
git pull origin ${BRANCH_NAME}

# 실행시킬 코드가 있는 브랜치로 체크아웃
git checkout ${BRANCH_NAME}

# 빌드
./gradlew bootJar

# 이동
cd ./build/libs

# 실행 중인 Spring Boot 애플리케이션 확인
PID=$(pgrep -f 'java -jar')

# -z: 문자열이 비여 있지 않은 경우에 참을 반환
if [ -z "$PID" ]
then
  # 애플리케이션 시작 명령어
  java -jar jwp-shopping-order.jar
else
  kill $PID
  # 애플리케이션 시작 명령어
  java -jar jwp-shopping-order.jar
fi