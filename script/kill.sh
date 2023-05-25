#!/bin/bash

if lsof -i :80; then
    echo "Port 80 is already in use. Killing the process..."
    lsof -i :80 | awk 'NR!=1 {print $2}' | xargs kill -9
fi
