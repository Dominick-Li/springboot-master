#:!/bin/bash

BASE_DIR=$(cd $(dirname $0);cd ..; pwd)
echo "Welcome enter $BASE_DIR"
cd $BASE_DIR

#启动的jar存放路径(需要换成你jar包的位置)
SERVER_NAME=build-app.jar

SERVER_PATH="lib/${SERVER_NAME}"
#程序的外部配置文件路径,这里使用了相对路径
CONFIG=conf/application.yml
#日志文件的路径
LOG_CONFIG=conf/logback-spring.xml
#nohup命令的日志输出路径,
CONSOLE_LOG_OUT=logs/server-console.log

#设置最小堆内存 最大堆内存 年轻代堆内存 初始化元空间内存  最大元空间内存 (元空间是jdk1.8才有的,jdk1.8之前叫永久代,jvm知识请阅读方志明老师写的深入理解Java虚拟机)
JAVA_OPT='-Xms512m -Xmx512m -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m'
#设置内存溢出打印堆日志
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/heapdump.hprof"
#设置GC输出日志 ,测试环境可以打印,生产环境需要注释掉
JAVA_OPT="${JAVA_OPT} -XX:+PrintGCDetails  -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:logs/gc.log"

#设置启动程序需要指定的spring配置信息
JAVA_OPT="${JAVA_OPT} -jar  ${SERVER_PATH} "
JAVA_OPT="${JAVA_OPT} --spring.config.location=${CONFIG}"
JAVA_OPT="${JAVA_OPT} --logging.config=${LOG_CONFIG}"

if [ ! -d "logs" ] ;
then
  echo 'mkdir logs dir'
  mkdir -p "logs"
fi

function start() {
    PID=$(ps -ef | grep $SERVER_NAME | grep -v grep | awk '{ print $2 }')
    if [ -z "$PID" ]
	    then
	    echo will start ...
    else
	    echo "Start fail, app runing. at $CURRENT_DIR, pid=$PID"
	    exit 1
    fi
    echo '-----print JAVA_OPT------'
    echo $JAVA_OPT
    #把启动命令里的  >$CONSOLE_LOG_OUT 2>&1 &  换成 >dev/null & 可以让nohup不输出启动日志,改成>dev/null的话记得把 tail -f
    nohup java $JAVA_OPT >$CONSOLE_LOG_OUT 2>&1 &
    #查看实时的启动日志
    tail -f $CONSOLE_LOG_OUT
}

function stop() {
    PID=$(ps -ef | grep $SERVER_NAME | grep -v grep | awk '{ print $2 }')
    if [ -z "$PID" ]
	    then
	    echo Application is already stopped
    else
	    echo kill $PID
	    kill  $PID
    fi
}

#根据启动脚本的第1个参数决定调用哪个方法
# 启动 ./server.sh  start
# 停止 ./server.sh  stop
# 重启 ./server.sh restart
case $1 in
    start)
      shift 1
      start $@
      ;;
    stop)
      shift 1
      stop
      ;;
    restart)
      shift 1
      stop
      sleep 4
      start $@
      ;;
esac
