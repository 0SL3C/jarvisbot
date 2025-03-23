#!/bin/bash
# Compile app
echo "Compiling files"
javac -cp "./Backend/lib/*" ./Backend/src/App.java -d ./Backend/bin

# Run
echo "Running app..."
java -cp "./Backend/bin:./Backend/lib/Ab.jar" App