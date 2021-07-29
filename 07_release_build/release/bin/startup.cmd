@echo off
@REM -----------------------------------------------------------------------------
@REM Start script for the app Server
@REM -----------------------------------------------------------------------------

setlocal enabledelayedexpansion
set errorlevel=
title build-app server

set CONFIG=conf/application.yml
set LOG_CONFIG=conf/logback-spring.xml

@REM 此处需要修改成你的jar包
set SERVER_NAME=build-app.jar
set SERVER_PATH=lib/%SERVER_NAME%


set JAVA_OPT=-Xms512m -Xmx512m -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m
set JAVA_OPT=%JAVA_OPT% -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/heapdump.hprof
set JAVA_OPT=%JAVA_OPT% -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:logs/gc.log

set JAVA_OPT=%JAVA_OPT% -jar  %SERVER_PATH%
set JAVA_OPT=%JAVA_OPT% --spring.config.location=%CONFIG%
set JAVA_OPT=%JAVA_OPT% --logging.config=%BASE_DIR%%LOG_CONFIG%

cd /d "%~dp0"
cd ..
set BASE_DIR=%~dp0
@REM Check if Java is correctly installed and set
java -version 1>nul 2>nul
if !errorlevel! neq 0 (
    @echo Please install Java 1.8 or higher and make sure the Java is set correctly.
    @echo You can execute command [ java -version ] to check if Java is correctly installed and set.
    pause
    goto:eof
)

@echo Please do not close the current window.
@echo -----print JAVA_OPT------
@echo %JAVA_OPT%
java %JAVA_OPT%
@echo Stopped %SERVER_JAR%
pause
goto:eof
