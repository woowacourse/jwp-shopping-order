#!/bin/bash

PORT=80
PROJECT_NAME="jwp-shopping-order"
PROJECT_DIR="$HOME/${PROJECT_NAME}"
REPOSITORY_URL="https://github.com/pilyang/jwp-shopping-order"
DEPLOY_BRANCH="step1"

# Step 1: Check if port 8080 is running and kill the process if it is
if lsof -i :${PORT}; then
    echo "Port ${PORT} is already in use. Killing the process..."
    lsof -i :${PORT} | awk 'NR!=1 {print $2}' | xargs kill -9
fi

# Step 2: Check if jwp-shopping-order directory exists, and clone it if it doesn't
if [ ! -d ${ROJECT_DIR} ]; then
    echo "Cloning ${PROJECT_NAME} repository..."
    git -C ~ clone --single-branch -b ${DEPLOY_BRANCH} ${REPOSITORY_URL}
fi

# Step 3: Checkout the 'step1' branch
echo "Checking out '${DEPLOY_BRANCH}' branch..."
git -C ${PROJECT_DIR} checkout ${DEPLOY_BRANCH}

# Step 4: Pull the latest changes from the 'step1' branch
echo "Pulling the latest changes from 'step1' branch..."
git -C ${PROJECT_DIR} pull origin ${DEPLOY_BRANCH}

# Step 5: Build the project using './gradlew clean bootJar'
echo "Building the project..."
cd ${PROJECT_DIR}
./gradlew clean bootJar

# Step 6: Run the application in the background using 'java -jar' and 'nohup'
echo "Starting the application..."
sudo nohup java -jar build/libs/jwp-shopping-order.jar --spring.profiles.active=prod &
