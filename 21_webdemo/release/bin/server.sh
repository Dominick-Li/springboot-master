#:!/bin/bash

BASE_DIR=$(cd $(dirname $0);cd ..; pwd)
echo "Welcome enter $BASE_DIR"

server_name=webdemo
server_jar="lib/${server_name}.jar"
console_out="logs/server-console-out.log"

#Set heap memory and Metaspace memory
JAVA_OPT="-Dloader.path=lib"
JAVA_OPT="${JAVA_OPT} -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/heapdump/${server_name}_heapdump.hprof"
#JAVA_OPT="${JAVA_OPT} -XX:+PrintGCDetails -Xloggc:logs/gc/${server_name}-gc.log"

cd $BASE_DIR

if [ ! -d "logs/gc" ] ;
then
  echo 'mkdir logs/gc'
  mkdir -p "logs/gc"
fi

if [ ! -d "logs/heapdump" ] ;
then
  echo 'mkdir logs/heapdump'
  mkdir -p "logs/heapdump"
fi



function start() {
    PID=$(ps -ef | grep $server_name | grep -v grep | awk '{ print $2 }')
    if [ -z "$PID" ]
	    then
	    echo will start ...
    else
	    echo "Start fail, app runing. at $BASE_DIR, pid=$PID"
	    exit 1
    fi
    #nohup java $JAVA_OPT -jar $server_jar --spring.config.location=$config_file>/dev/null &
    nohup java $JAVA_OPT -jar $server_jar >$console_out 2>&1 &
    tail -f $console_out
}

function stop() {
    _kill
}

function _kill() {
    PID=$(ps -ef | grep $server_name | grep -v grep | awk '{ print $2 }')
    if [ -z "$PID" ]
	    then
	    echo Application is already stopped
    else
	    echo kill $PID
	    kill $PID
    fi
}

case $1 in
    start)
      shift 1
      start $@
      ;;
    stop)
      shift 1
      stop
      ;;
    kill)
      shift 1
      _kill $@
      ;;
    restart)
      shift 1
      stop
      sleep 4
      start $@
      ;;
esac
