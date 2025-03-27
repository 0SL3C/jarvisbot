#!/bin/bash

# Compile all Java files
echo "Compiling Java files..."
CLASSPATH="./Backend/lib/*"
javac -cp "$CLASSPATH" ./Backend/src/*.java -d ./Backend/bin

# Run server
echo "Starting Tomcat server..."
java -cp "./Backend/bin:$CLASSPATH" ServletMain