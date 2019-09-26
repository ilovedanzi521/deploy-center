#!/bin/sh
# "init.sh <remote_addr> [remote_port]
echo "Start $0 $*"

# initialize env variable
# manual configure FTP_SERVER and FTP_PORT
CC_FTP_SERVER=192.168.0.70
CC_FTP_PORT=21
CC_FTP_ROOT=/pub/repo
CC_FTP_USER=ftp
CC_FTP_PSWD=ftp

CC_HOME_DIR=~/repo
CC_PACKAGES_DIR=$CC_HOME_DIR/packages
CC_PACKAGES_TOOL_DIR=$CC_HOME_DIR/packages/tools
CC_MODULES_DIR=$CC_HOME_DIR/modules
CC_SCRIPTS_DIR=$CC_HOME_DIR/scripts

CC_DEPLOY_DIR=~/deploy
CC_BACKUP_DIR=$CC_DEPLOY_DIR/backup
CC_LOG_DIR=$CC_DEPLOY_DIR/logs

# Get remote host from first parameter
REMOTE_HOST=
REMOTE_PORT=22
if [ $# -eq 1 ];then
  REMOTE_HOST=$1
elif [ $# -eq 2 ];then
  REMOTE_HOST=$1
  REMOTE_PORT=$2
else
  echo "ERROR: remote host unknown."
  exit 10
fi

echo "paramNum:$#, REMOTE_HOST=$REMOTE_HOST:$REMOTE_PORT ftp_server=$CC_FTP_SERVER ftp_port=$CC_FTP_PORT"

# init generate env.sh
cat << EOF > $CC_HOME_DIR/env.sh
#!/bin/sh
CC_FTP_SERVER=$CC_FTP_SERVER
CC_FTP_PORT=$CC_FTP_PORT
CC_FTP_ROOT=$CC_FTP_ROOT
CC_FTP_USER=$CC_FTP_USER
CC_FTP_PSWD=$CC_FTP_PSWD
export CC_FTP_SERVER CC_FTP_PORT CC_FTP_ROOT CC_FTP_USER CC_FTP_PSWD

CC_HOME_DIR=$CC_HOME_DIR
CC_PACKAGES_DIR=$CC_PACKAGES_DIR
CC_PACKAGES_TOOL_DIR=$CC_PACKAGES_TOOL_DIR
CC_MODULES_DIR=$CC_MODULES_DIR
CC_SCRIPTS_DIR=$CC_SCRIPTS_DIR
export CC_HOME_DIR CC_MODULES_DIR CC_SCRIPTS_DIR

CC_DEPLOY_DIR=$CC_DEPLOY_DIR
CC_BACKUP_DIR=$CC_BACKUP_DIR
CC_LOG_DIR=$CC_LOG_DIR
export CC_DEPLOY_DIR CC_LOG_DIR CC_BACKUP_DIR

source $CC_HOME_DIR/common.sh

EOF

chmod u+x $CC_HOME_DIR/env.sh && . $CC_HOME_DIR/env.sh
chmod u+x $CC_HOME_DIR/ftp_client.sh

# create remote dir
ssh -p $REMOTE_PORT $REMOTE_HOST \
mkdir -p $CC_HOME_DIR \
$CC_PACKAGES_DIR $CC_MODULES_DIR $CC_SCRIPTS_DIR $CC_PACKAGES_TOOL_DIR \
$CC_DEPLOY_DIR $CC_BACKUP_DIR $CC_LOG_DIR 

echo "create remote dir ret=$?"

# scp env.sh&ftp_client.sh to remote host
scp -P $REMOTE_PORT $CC_HOME_DIR/env.sh \
	$CC_HOME_DIR/shell.sh \
	$CC_HOME_DIR/ftp_client.sh \
	$CC_HOME_DIR/common.sh \
	$REMOTE_HOST:$CC_HOME_DIR/

if [ $? -ne 0 ];then
  echo "remote host $REMOTE_HOST:$REMOTE_PORT connect failed."
  exit 11
fi
scp -P $REMOTE_PORT $CC_MODULES_DIR/* $REMOTE_HOST:$CC_MODULES_DIR/
scp -P $REMOTE_PORT $CC_SCRIPTS_DIR/* $REMOTE_HOST:$CC_SCRIPTS_DIR/
scp -P $REMOTE_PORT $CC_PACKAGES_DIR/ftp $REMOTE_HOST:$CC_PACKAGES_DIR/

echo "scp -P $REMOTE_PORT $CC_HOME_DIR/env.sh $CC_HOME_DIR/ftp_client.sh $CC_HOME_DIR/common.sh $REMOTE_HOST:$CC_HOME_DIR/"
echo "scp -P $REMOTE_PORT $CC_MODULES_DIR/ $REMOTE_HOST:$CC_MODULES_DIR"

# Execute remote chmod +x to env.sh&ftp_client.sh
ssh $REMOTE_HOST -p $REMOTE_PORT chmod u+x $CC_HOME_DIR/env.sh \
		$CC_HOME_DIR/shell.sh \
		$CC_HOME_DIR/ftp_client.sh \
		$CC_PACKAGES_DIR/ftp

cat << EOF
ssh $REMOTE_HOST -p $REMOTE_PORT chmod u+x $CC_HOME_DIR/env.sh 
		$CC_HOME_DIR/shell.sh 
		$CC_HOME_DIR/ftp_client.sh 
		$CC_PACKAGES_DIR/ftp

echo "initialize OK."
EOF


