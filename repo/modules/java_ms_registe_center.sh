#!/bin/sh

# 1. Define Constants
ModuleName="Java-service-registeCenter"
ModuleVersion="1.0"
ModuleDescription="Java微服务-注册中心应用"

# 2. Source env.sh
if [ -z $CC_HOME_DIR ];then
  source ~/repo/env.sh
else
  source $CC_HOME_DIR/env.sh
fi

# 3. Get Parameter
#echo $@
argStr=`echo $@ | tr '=' ' '`	# 把参数paramKey=paramValue转换成paramKey paramValue
Getops $argStr
#echo "Get Parameter: GID=$GID HOST=$HOST INSTALL=$INSTALL_DIR BACKUP_DIR=$BACKUP_DIR LOG_DIR=$LOG_DIR 
#      PACK_VER=$PACK_VER $PACK_DIR=$PACK_DIR PACK_FILE=$PACK_FILE 
#      JAR_FILE=$JAR_FILE JDK_DIR=$JDK_DIR JDK_VER=$JDK_VER JDK_FILE=$JDK_FILE"

AppName=$PACK_FILE

Name() {
	echo $ModuleName
}

Version() {
	echo $ModuleVersion
}

Path() {
	basepath=$(cd `dirname $0`; pwd)
	filename=`basename $0`
	echo $basepath/$filename
}

PackagePath() {
	echo "$CC_HOME_DIR/$PACK_DIR/$PACK_VER/$PACK_FILE"
}

Description() {
	return $ModuleDescription
}

Help(){
	echo "help"
}

Start(){
  appName=$AppName
  appPath=$PACK_DIR/$PACK_VER/$PACK_FILE
  absAppPath=$CC_HOME_DIR/$PACK_DIR/$PACK_VER/$PACK_FILE
  echo "Start Service"

  # Start java_ms_xxx service
  if test -r $absAppPath
  then
    $CC_HOME_DIR/modules/java_sdk.sh --JAR_FILE=$appPath --JDK_DIR=$JDK_DIR --JDK_VER=$JDK_VER
  else
    echo "Can not founds $appPath, code=2"
  fi

  # Check java_ms_xxx is running
  total=10
  take=1
  while [ 1 ]
  do
    Status
    pid=$_PID
    if [ $pid -gt 0 ]; then
      echo "Service already running, PID: $pid"
      return 1
    fi
     
    total=`expr $total - $take`
    if [ $total -le 0 ];then
      echo "Service has not Running, PID: $pid"
      return 0
    else
      sleep $take
    fi
  done 
  return 0
}

# return process is runnig?
_PID=
Status(){
  _PID=0
  absAppPath=`PackagePath`
  pstr=`ps -ef | grep "jar $absAppPath" | grep -vE "$0|grep" | head -n1`
  array=(${pstr/,/ /})
  pid=${array[1]}

  #echo $pstr
  #echo "Status pstr=$pstr"
  #echo "Status pid=$pid"
  if [ -z $pid ];then
    _PID=0
    echo "not running $_PID"
    return 0
  else 
    _PID=$pid
    echo "running $_PID"
    return 1
  fi
}

Stop(){
  Status
  spid=$_PID
  echo "Stop $AppName pid=$spid"

  # Check java_ms_xxx is running
  total=10
  take=1
  while [ 1 ]
  do
    Status
    pid=$_PID
    if [ $pid -le 0 ];then
      echo "Service already killed"
      return 1
    fi
     
    total=`expr $total - $take`
    if [ $total -gt 0 ];then
      echo "Service is Running, PID: $pid"
      if [ $pid -gt 0 ]; then
        echo "kill -9 $pid"
        kill -9 $pid
      fi
    else
      sleep $take
    fi
  done 
  return 0
}

Restart(){
    Stop
    Start
}

# return app is install?
Check() {
  absAppPath=`PackagePath`

  if [ -r $absAppPath ];then
    echo 1
  else
    echo 0
  fi
}

Install(){
  echo "Install"
  appName=$AppName
  appPath="$PACK_DIR/$PACK_VER/$PACK_FILE"
  absAppPath=`PackagePath`

  # Check AppName is download
  fileIsExit=`Check`
  echo "$appPath is exist $fileIsExit"

  # Check and Stop java_ms_trade is running
  Stop

  # ftp get java_ms_trade_xxx.jar and copy to deploy dir
  echo "$CC_HOME_DIR/ftp_client.sh get --PACK_TYPE=binary --PACK_DIR=$PACK_DIR --PACK_VER=$PACK_VER --PACK_FILE=$PACK_FILE"

  $CC_HOME_DIR/ftp_client.sh get --PACK_TYPE=binary --PACK_DIR=$PACK_DIR --PACK_VER=$PACK_VER --PACK_FILE=$PACK_FILE 
  code=$?
  if [ $code -ne 0 ];then
    echo "ftp get $appPath failed, code=$code" 
    exit 2
  else
    echo "ftp get $appPath OK"
  fi

  # Check file is exit
  fileIsExit=`Check`
  if [ $fileIsExit -gt 0 ];then
    echo "Install success."
  else
    echo "Install failed."
  fi
}

Uninstall() {
  # Check and Stop
  Stop

  appName=$PACK_FILE
  appDir=$CC_HOME_DIR/$PACK_DIR/$PACK_VER/
  absAppPath=`PackagePath`
  echo "Uninstall $absAppPath"

  code=1
  if [ -d $appDir ];then
    echo "rm -f $appDir"
    rm -rf $appDir
    code=$?
    echo "Uninstall $code"
  else
    echo "Uninstall not founds $appDir"
  fi

  return $code
}

Update(){
  Install $@
}

case $1 in
  'name')
	Name
    	;;
  'version')
	Version
    	;;
  'path')
	Path
    	;;
  'packpath')
	PackagePath
    	;;
  'desc')
	Description
	;;
  'help')
	Help	
    	;;
  'start')
	Start
    	;;
  'stop')
	Stop	
    	;;
  'status')
	Status	
    	;;
  'restart')
	Stop
	Start
    	;;
  'install')
	Install	$@
    	;;
  'uninstall')
	Uninstall	
    	;;
  'update')
	Update
    	;;
  'check')
	Check
    	;;
  *)
  exit 1
esac
exit 0

