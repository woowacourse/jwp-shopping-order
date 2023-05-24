#!/bin/bash

# Step 2: Check if jwp-shopping-order directory exists, and clone it if it doesn't
if [ ! -d "jwp-shopping-order" ]; then
    echo "Cloning jwp-shopping-order repository..."
    git clone --single-branch -b step1 https://github.com/be-student/jwp-shopping-order
fi

# Step 3: Checkout the 'step1' branch
cd jwp-shopping-order
echo "Checking out 'step1' branch..."
git checkout step1

# Step 4: Pull the latest changes from the 'step1' branch
echo "Pulling the latest changes from 'step1' branch..."
git pull origin step1

# Step 5: Build the project using './gradlew clean bootJar'
echo "Building the project..."
./gradlew clean bootJar

# Step 1: Check if port 80 is running and kill the process if it is
if lsof -i :80; then
    echo "Port 80 is already in use. Killing the process..."
    lsof -i :80 | awk 'NR!=1 {print $2}' | xargs kill -9
fi

# Step 6: Run the application in the background using 'java -jar' and 'nohup'
echo "Starting the application..."
sudo nohup java -jar build/libs/jwp-shopping-order.jar --spring.profiles.active=prod &
