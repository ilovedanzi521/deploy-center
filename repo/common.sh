#!/bin/sh

HOST=
HOSTLIST=
INSTALL_DIR=
BACKUP_DIR=
LOG_DIR=
PACK_VER=
PACK_DIR=
PACK_FILE=
PACK_TYPE=
JAR_FILE=
JDK_HOME=
JDK_VER=
JDK_FILE=

Getops() {
while [ $# -gt 0 ];
do
  case "$1" in
	--HOSTLIST)
	  shift;
	  GID=$1;
	  shift;
	  ;;
	--HOST) 
	  shift;
	  HOST=$1;
	  shift;
	  ;;
        --INSTALL_DIR)
	  shift;
	  INSTALL_DIR=$1;
	  shift;
	  ;;
	--BACKUP_DIR)  
	  shift;
	  BACKUP_DIR=$1;
	  shift;
	  ;;
	--LOG_DIR)  
	  shift;
	  LOG_DIR=$1;
	  shift;
	  ;;
	--PACK_DIR)  
	  shift;
	  PACK_DIR=$1;
	  shift;
	  ;;
	--PACK_VER)  
	  shift;
	  PACK_VER=$1;
	  shift;
	  ;;
	--PACK_FILE)  
	  shift;
	  PACK_FILE=$1;
	  shift;
	  ;;
	--PACK_TYPE)
	  shift;
	  PACK_TYPE=$1;
	  shift;
	  ;;
	--JAR_FILE)
	  shift;
	  JAR_FILE=$1;
	  shift;
	  ;;
	--JDK_FILE)
	  shift;
	  JDK_FILE=$1;
	  shift;
	  ;;
	--JDK_DIR)
	  shift;
	  JDK_DIR=$1;
	  shift;
	  ;;
	--JDK_VER)
	  shift;
	  JDK_VER=$1;
	  shift;
	  ;;
	*)
	  shift;
	  ;;
  esac
done
}

FtpGet() {
ftp -in $CC_FTP_SERVER $CC_FTP_PORT <<EOF
	user $CC_FTP_USER $CC_FTP_PSWD
	$filetype
	get $CC_FTP_ROOT/$filepath $localrepo/$filepath
EOF
}

#echo GID=$GID HOST=$HOST INSTALL=$INSTALL_DIR BACKUP_DIR=$BACKUP_DIR LOG_DIR=$LOG_DIR PACK_VER=$PACK_VER PACK_DIR=$PACK_DIR PACK_FILE=$PACK_FILE JAR_FILE=$JAR_FILE
