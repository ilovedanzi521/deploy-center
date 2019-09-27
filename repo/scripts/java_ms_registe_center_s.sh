#!/bin/sh

# 1. Define Constants
StrategyName="RegisteCenterStrategy"
StrategyVersion="1.0"
StrategyDescription="注册中心的部署策略"

# 2. Source env.sh
#echo $0 $*
#echo $StrategyName $StrategyVersion $StrategyDescription
if [ -z $CC_HOME_DIR ];then
  source ~/repo/env.sh
else
  source $CC_HOME_DIR/env.sh
fi

# 3. 重要: 定义该策略中使用的应用模块, 便于配置机器和服务的映射关系
ModulesArray=(
	"java_sdk.sh"
	"java_ms_registe_center.sh"
)

# 定义策略需要使用的jdk模块
JDKModulePath=$CC_HOME_DIR/modules/java_sdk.sh	
JavaServiceModulePath=$CC_HOME_DIR/modules/java_ms_registe_center.sh

# 4. Get Parameter
#echo $@
argStr=`echo $@ | tr '=' ' '`	# 把参数paramKey=paramValue转换成paramKey paramValue
Getops $argStr
#echo "Get Parameter: GID=$GID HOST=$HOST INSTALL=$INSTALL_DIR BACKUP_DIR=$BACKUP_DIR LOG_DIR=$LOG_DIR 
#      PACK_VER=$PACK_VER PACK_DIR=$PACK_DIR PACK_FILE=$PACK_FILE 
#      JDK_DIR=$JDK_DIR JDK_VER=$JDK_VER JDK_FILE=$JDK_FILE"

Name() {
	echo $StrategyName
}

Path() {
	basepath=$(cd `dirname $0`; pwd)
	filename=`basename $0`
	echo $basepath/$filename
}

Version(){
	echo $ModuleVersion
}

Description() {
	echo $StrategyDescription
}

Help(){
cat << EOF
  java_ms_registe_center_s.sh [command] [params]
  commands:
    name: 
    path:
    desc:
    help:
    deploy:
      eg: java_ms_registe_center_s.sh deploy --JDK_DIR=packages --JDK_VER=jdk1.8.0_221 --JDK_FILE=jdk-8u221-linux-x64.tar.gz \
                                             --PACK_DIR=packages/java_ms --PACK_VER=1.0 --PACK_FILE=dfas-register-5.1.3.33.jar
    undeploy:
      eg: java_ms_registe_center_s.sh undeploy --JDK_DIR=packages --JDK_VER=jdk1.8.0_221 --JDK_FILE=jdk-8u221-linux-x64.tar.gz \
                                             --PACK_DIR=packages/java_ms --PACK_VER=1.0 --PACK_FILE=dfas-register-5.1.3.33.jar

    list_modules: list all dependents modules.
      eg:
        ./java_ms_registe_center_s.sh list_modules
        java_sdk.sh
        java_ms_registe_center.sh
EOF
}

Deploy() {
  echo "[Deploy start...]"

  echo "[Deploy uninstall java-sdk]"
  $JDKModulePath uninstall --JDK_VER=$JDK_VER --JDK_DIR=$JDK_DIR --JDK_FILE=$JDK_FILE
  echo "[Deploy uninstall java-sdk done]"

  echo "[Deploy install java-sdk]"
  $JDKModulePath install --JDK_VER=$JDK_VER --JDK_DIR=$JDK_DIR --JDK_FILE=$JDK_FILE
  echo "[Deploy install java-sdk done]"

  echo "[Deploy uninstall java-micro-service]"
  $JavaServiceModulePath uninstall --PACK_DIR=$PACK_DIR --PACK_VER=$PACK_VER --PACK_FILE=$PACK_FILE
  echo "[Deploy uninstall java-micro-service done]"

  echo "[Deploy install java-micro-service]"
  $JavaServiceModulePath install --PACK_DIR=$PACK_DIR --PACK_VER=$PACK_VER --PACK_FILE=$PACK_FILE
  echo "[Deploy install java-micro-service done]"

  echo "[Deploy $JavaServiceModulePath start...]"
  $JavaServiceModulePath start --PACK_DIR=$PACK_DIR --PACK_VER=$PACK_VER --PACK_FILE=$PACK_FILE --JDK_DIR=$JDK_DIR --JDK_VER=$JDK_VER
  echo "[Deploy $JavaServiceModulePath start done]"

  echo "[Deploy done.]"
}

Undeploy() {
  echo "Undeploy"

  $JavaServiceModulePath uninstall --PACK_DIR=$PACK_DIR --PACK_VER=$PACK_VER --PACK_FILE=$PACK_FILE 

  $JDKModulePath uninstall --JDK_DIR=$JDK_DIR --JDK_VER=$JDK_VER --JDK_FILE=$JDK_FILE

  echo "Undeploy done."
}

ListModules() {
  for name in ${ModulesArray[@]}
  do
    echo ${name}
  done
}

case $1 in
  'name')
	Name
	;;
  'path')
	Path
	;;
  'desc')
	Description
	;;
  'help')
	Help
	;;
  'deploy')
	Deploy
	;;
  'undeploy')
	Undeploy
	;;
  'list_modules')
        ListModules
	;;
  *)
  exit 1
esac
exit 0
