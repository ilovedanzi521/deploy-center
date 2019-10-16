#!/bin/sh

# 1. Define Constants
ModuleName="Java-SDK"
ModuleVersion=1.0
ModuleDescription="Java SDK 安装执行模块v$ModuleVersion"

# 2. Source env.sh
#echo $0 $*
#echo $ModuleName $ModuleVersion $ModuleDescription
if [ -z $CC_HOME_DIR ];then
  source ~/repo/env.sh
else
  source $CC_HOME_DIR/env.sh
fi

# 3. Get Parameter
argStr=`echo $@ | tr '=' ' '`	# 把参数paramKey=paramValue转换成paramKey paramValue
Getops $argStr
#echo "Get Parameter: GID=$GID HOST=$HOST INSTALL=$INSTALL_DIR BACKUP_DIR=$BACKUP_DIR LOG_DIR=$LOG_DIR \
#      JDK_VER=$JDK_VER $JDK_DIR=$JDK_DIR $JDK_FILE=$JDK_FILE JAR_FILE=$JAR_FILE"

PackDir=$JDK_DIR					# packages/ 相对与repo目录
PackUnzipDir=$JDK_VER 					# jdk1.8.0_221 linux安装包解压后的目录名
PackageFile=$JDK_FILE					# jdk-8u221-linux-x64.tar.gz
JdkInstallDir=$CC_DEPLOY_DIR/java/$PackUnzipDir		# java sdk安装目录
JavaCmd=$JdkInstallDir/bin/java				# java bin执行文件位置
JarFile=$JAR_FILE

Name() {
	echo $ModuleName
}

Version(){
	echo $ModuleVersion
}

JdkVersion(){
	echo $JDK_VER
}

Path() {
	basepath=$(cd `dirname $0`; pwd)
	filename=`basename $0`
	echo $basepath/$filename
}

PackagePath() {
	echo $CC_HOME_DIR/$JDK_DIR/$JDK_VER/$JDK_FILE
}

Description() {
	return ModuleDescription
}

Help(){
cat << EOF
  java_sdk.sh [command]
    eg: ./java_sdk.sh ~/repo/package/java_ms/eureka-server-0.0.1-SNAPSHOT.jar
        java -jar ~/repo/package/java_ms/eureka-server-0.0.1-SNAPSHOT.jar
  commands:
    version: get ftp client version string.
    jdk-version: print java sdk version, eg:jdk1.8.0_221
    name: return ModuleName.
    path: return name relative from CC_HOME_DIR(repo root).
    packpath: return name package file for absolute path.
    help: print this module usage.
  
    install:
         eg. java_sdk.sh install \
			 --JDK_DIR=packages \
			 --JDK_VER=jdk1.8.0_221 \
			 --JDK_FILE=jdk-8u221-linux-x64.tar.gz
    uninstall:
    update:
    check:
    jar: --jar --PACKAGE_DIR= --JDK_DIR= --JDK_VER= --JDK_FILE=
         eg. java_sdk.sh --JAR_FILE=packages/java_ms/1.0/dfas-register-5.1.3.33.jar \
			 --JDK_DIR=packages \
			 --JDK_VER=jdk1.8.0_221
EOF
}

Start(){
  return 0
}

Stop(){
  return 0
}

Restart(){
	echo "no implements Restart"
}

Status(){
  return 0
}

Java() {
  echo "$JavaCmd -jar $CC_HOME_DIR/$JarFile"

  echo "JAVA_HOME=$JAVA_HOME"
  echo "JRE_HOME=$JRE_HOME"
  echo "CLASSPATH=$CLASSPATH"
  echo "PATH=$PATH"

  if [ -x $JavaCmd ];then
    setJavaEnv
    nohup $JavaCmd -jar $CC_HOME_DIR/$JarFile > /tmp/nohup.out 2>&1 &
  else
    echo "ERROR: $JavaCmd not founded."
  fi
}

setJavaEnv() {
  echo "setJavaEnv"

  java_home=$JdkInstallDir
  unset JAVA_HOME
  unset JRE_HOME
  unset CLASSPATH
  echo "unset JAVA_HOME JRE_HOME CLASSPATH"

  export JAVA_HOME=$java_home
  export JRE_HOME=$JAVA_HOME/jre
  export CLASSPATH=$JAVA_HOME/lib:$JRE_HOME/lib
  export PATH=$JAVA_HOME/bin:$PATH

  echo "export JAVA_HOME JRE_HOME CLASSPATH"
  echo "JAVA_HOME=$JAVA_HOME"
  echo "JRE_HOME=$JRE_HOME"
  echo "CLASSPATH=$CLASSPATH"
  echo "PATH=$PATH"
}

Uninstall() {
  echo "Uninstall $ModuleName $ModuleVersion for jdk version=$JDK_VER"

  java_home=$JdkInstallDir
  jdk_package_locfile=`PackagePath`
  unset JAVA_HOME
  unset JRE_HOME
  unset CLASSPATH

  code=0
  if test -d $java_home
  then
    echo "rm -rf $java_home"
    rm -rf $java_home
    code=$?
    echo "rm -rf $java_home. $code"
  fi

  if test -w $jdk_package_locfile
  then
    echo "rm -f $jdk_package_locfile"
    rm -f $jdk_package_locfile
    echo "rm -f $jdk_package_locfile. $?"
  fi
  echo "Uninstall done. $code"
}

Install(){
  echo "install $ModuleName $ModuleVersion for jdk version=$PackUnzipDir"

  # 1. ftp get newer java-sdk package
  java_home=$JdkInstallDir
  java_cmd=$JavaCmd
  jdk_package=$PackageFile
  jdk_package_locfile=`PackagePath`

  $CC_HOME_DIR/ftp_client.sh get --PACK_TYPE=binary --PACK_DIR=$JDK_DIR --PACK_VER=$JDK_VER --PACK_FILE=$JDK_FILE 
  code=$?
  if [ $code -ne 0 ];then
    echo "ftp get $jdk_package failed, code=$code"
    exit $code
  fi

  # 2. Uninstall java Uninstall  
  if [ ! -r $jdk_package_locfile ];then
    echo "$jdk_package_locfile not founded."
    exit 2
  fi

  # 3. Extra $jdk_package to $java_home
  echo "Extra $jdk_package_locfile to $CC_DEPLOY_DIR/java"
  mkdir -p $CC_DEPLOY_DIR/java
  tar -zxvf $jdk_package_locfile -C $CC_DEPLOY_DIR/java > /dev/null

  code=$?
  if [ $code -ne 0 ];then
    echo "Extra failed. code=$code"
    exit $code
  fi

  if test -d $java_home
  then
    setJavaEnv
  fi
}

Update(){
  Install $@
}

Check(){
  if [ -x $JavaCmd ];then
    #echo "check $JavaCmd true"
    return 1
  else
    #echo "check $JavaCmd false"
    return 2
  fi
}

case $1 in
  'name')
	Name	
    	;;
  'version')
	Version
    	;;
  'jdk-version')
	JdkVersion
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
  'jar')
	Java $@ 
	;;
  *)
	Java $@ 
	exit 1
	;;
esac
exit 0

