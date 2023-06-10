GIT_USERNAME=yujamint
GIT_REPOSITORY=https://github.com/$GIT_USERNAME/jwp-shopping-order
PROJECT_PATH=~/Desktop/workspace/wooteco/jwp-shopping-order
BUILD_PATH=$PROJECT_PATH/build/libs
BUILD_JAR=$BUILD_PATH/jwp-shopping-order.jar

git -C $PROJECT_PATH pull $GIT_REPOSITORY

"$PROJECT_PATH/gradlew" -p "$PROJECT_PATH" bootJar

CURRENT_PID=`lsof -i :8080 -t`
if [ -z $CURRENT_PID ]
then
  echo "실행되고 있는 애플리케이션이 없습니다."
else
  echo "기존에 실행되고 있던 애플리케이션을 종료했습니다."
  kill -9 $CURRENT_PID
  sleep 3
fi

nohup java -jar $BUILD_JAR >> $BUILD_PATH/deploy.log 2> $BUILD_PATH/deploy-err.log &
