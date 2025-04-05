@echo off

echo Compiling Java files...
set CLASSPATH=./Backend/lib/*
set JAVAFILES=./Backend/src/*.java

javac -cp "%CLASSPATH%" %JAVAFILES% -d ./Backend/bin

REM Run server
echo Starting server...
java -cp "./Backend/bin;%CLASSPATH%" ServletMain