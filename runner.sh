#!/bin/sh

PROCESS_ID=$(lsof -t -i:8080)

if [ -n "$PROCESS_ID" ]; then
        echo 8080 포트 끄기
        kill  $PROCESS_ID
fi

echo `./gradlew clean bootJar`
echo `nohup java -jar ./build/libs/jwp-shopping-order.jar > spring.log &`
exit
