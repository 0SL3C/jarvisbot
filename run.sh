#!/bin/bash

# Compile all Java files
echo "Compiling Java files..."
CLASSPATH='./Backend/lib/*'
JAVAFILES='./Backend/src/*.java'

javac -cp "$CLASSPATH" "$JAVAFILES" -d ./Backend/bin
# javac -cp "chatbot/Backend/lib/*" "chatbot/Backend/src/*.java" -d "chatbot/Backend/bin/"
# javac -cp '/c/Users/miche/Desktop/chatbot/Backend/lib/\*' '/c/Users/miche/Desktop/chatbot/Backend/src/\*.java' -d "/c/Users/miche/Desktop/chatbot/Backend/bin/"

# Run server
# echo "Starting Tomcat server..."
java -cp "./Backend/bin:$CLASSPATH" ServletMain

