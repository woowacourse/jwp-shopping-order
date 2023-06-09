#!/bin/bash

DEPLOY_BRANCH="step2"
PORT=80

# Step 1: Check if port 80 is running and kill the process if it is
if lsof -i :${PORT}; then
    echo "Port ${PORT} is already in use. Killing the process..."
    lsof -i :${PORT} | awk 'NR!=1 {print $2}' | xargs kill -9
fi

# Step 2: Check if jwp-shopping-order directory exists, and clone it if it doesn't
if [ ! -d "jwp-shopping-order" ]; then
    echo "Cloning jwp-shopping-order repository..."
    git clone --single-branch -b ${DEPLOY_BRANCH} https://github.com/beer-2000/jwp-shopping-order
fi

# Step 3: Checkout the 'step1' branch
cd jwp-shopping-order
echo "Checking out 'step1' branch..."
git checkout ${DEPLOY_BRANCH}

# Step 4: Pull the latest changes from the 's1tep1' branch
echo "Pulling the latest changes from 'step1' branch..."
git pull origin ${DEPLOY_BRANCH}

# Step 5: Build the project using './gradlew clean bootJar'
echo "Building the project..."
./gradlew clean bootJar

# Step 6: Run the application in the background using 'java -jar' and 'nohup'
echo "Starting the application..."
sudo java -jar build/libs/jwp-shopping-order.jar --spring.profiles.active=prod &
