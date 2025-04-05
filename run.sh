#!/bin/bash

# Check if API_KEY is already set
if [ -z "$API_KEY" ]; then
    read -p "Enter your API key: " API_KEY
    export API_KEY
else
    echo "Using existing API_KEY environment variable: $API_KEY"
fi

# Compile all Java files
echo "Compiling Java files..."
CLASSPATH="./Backend/lib/*"
javac -cp "$CLASSPATH" ./Backend/src/*.java -d ./Backend/bin

# Run server
echo "Starting Tomcat server..."
java -cp "./Backend/bin:$CLASSPATH" ServletMain