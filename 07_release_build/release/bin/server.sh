#:!/bin/bash

BASE_DIR=$(cd $(dirname $0);cd ..; pwd)
echo "Welcome enter $BASE_DIR"
cd $BASE_DIR

#������jar���·��(��Ҫ������jar����λ��)
SERVER_NAME=build-app.jar

SERVER_PATH="lib/${SERVER_NAME}"
#������ⲿ�����ļ�·��,����ʹ�������·��
CONFIG=conf/application.yml
#��־�ļ���·��
LOG_CONFIG=conf/logback-spring.xml
#nohup�������־���·��,
CONSOLE_LOG_OUT=logs/server-console.log

#������С���ڴ� �����ڴ� ��������ڴ� ��ʼ��Ԫ�ռ��ڴ�  ���Ԫ�ռ��ڴ� (Ԫ�ռ���jdk1.8���е�,jdk1.8֮ǰ�����ô�,jvm֪ʶ���Ķ���־����ʦд���������Java�����)
JAVA_OPT='-Xms512m -Xmx512m -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m'
#�����ڴ������ӡ����־
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/heapdump.hprof"
#����GC�����־ ,���Ի������Դ�ӡ,����������Ҫע�͵�
JAVA_OPT="${JAVA_OPT} -XX:+PrintGCDetails  -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:logs/gc.log"

#��������������Ҫָ����spring������Ϣ
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
    #�������������  >$CONSOLE_LOG_OUT 2>&1 &  ���� >dev/null & ������nohup�����������־,�ĳ�>dev/null�Ļ��ǵð� tail -f
    nohup java $JAVA_OPT >$CONSOLE_LOG_OUT 2>&1 &
    #�鿴ʵʱ��������־
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

#���������ű��ĵ�1���������������ĸ�����
# ���� ./server.sh  start
# ֹͣ ./server.sh  stop
# ���� ./server.sh restart
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
