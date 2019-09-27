#!/bin/sh

# Define Constants
ModuleName="FTP-Client"
ModuleVersion=3.0.2
ModuleDescription="FTP client $ModuleVersion"

#echo $0 $*
#echo $ModuleName $Version
if [ -z $CC_HOME_DIR ];then
  source ~/repo/env.sh
else
  source $CC_HOME_DIR/env.sh
fi

ftpPath=
if test -x $ftpPath
then
  ftpPath="$CC_PACKAGES_DIR/ftp"
else
  ftpPath="ftp"
fi

# Get parameter
argStr=`echo $@ | tr '=' ' '`	# 把参数paramKey=paramValue转换成paramKey paramValue
Getops $argStr
echo "$0 Get Parameter: INSTALL_DIR=$INSTALL_DIR PACK_TYPE=$PACK_TYPE PACK_VER=$PACK_VER PACK_DIR=$PACK_DIR PACK_FILE=$PACK_FILE"

Name() {
	echo $ModuleName
}

Path() {
	basepath=$(cd `dirname $-1`; pwd)
	filename=`basename $0`
	echo $basepath/$filename
}

PackagePath() {
	echo $CC_PACKAGES_DIR/ftp
}

Description() {
	return ModuleDescription
}

Version() {
	echo $ModuleVersion
}

Help() {
cat << EOF
  ftp_client.sh <command> [params: [key]=[value]]
  commands:
    version: get ftp client version string.
    get <params> : eg, --PACK_TYPE=binary --PACK_DIR=packages/java/ --PACK_VER=1.8 --PACK_FILE=jdk-8u221-linux-x64.tar.gz
    name: return ModuleName.
    path: return name relative from CC_HOME_DIR(repo root).
    packpath: return name package file for absolute path.
    help: print this module usage.
  params:
    --PACK_TYPE: [ascii|binary]
    --PACK_DIR:  文件所在的相对目录, eg: packages/java/
    --PACK_VER:  文件的版本, eg: 1.8
    --PACK_FILE: 文件名
EOF
}

Get() {
	remoterepo=$CC_FTP_ROOT
        remotefile=$remoterepo/$PACK_DIR/$PACK_VER/$PACK_FILE
	localrepo=$CC_HOME_DIR
	localdir=$localrepo/$PACK_DIR/$PACK_VER
	localfile=$localrepo/$PACK_DIR/$PACK_VER/$PACK_FILE
	filetype=$FTP_TYPE

	if [ -z $filetype ];then
	  filetype="binary"
	fi

 	mkdir -p $localdir

	echo "$ftpPath get $remotefile $localfile"
	echo "FTP_SERVER: $CC_FTP_SERVER $CC_FTP_PORT $CC_FTP_USER"

$ftpPath -in $CC_FTP_SERVER $CC_FTP_PORT << EOF
	user $CC_FTP_USER $CC_FTP_PSWD
	$filetype
	get $remotefile $localfile
EOF
	ls -l $localfile
}

case $1 in
  'name')
	Name
    	;;
  'path')
	Path	
    	;;
  'version')
	Version
    	;;
  'help')
	Help	
    	;;
  'get')
	Get $@
    	;;
  'install')
	Install	
    	;;
  'update')
	Update
    	;;
  'check')
	Check
	Start
    	;;
  *)
  exit 1
esac

