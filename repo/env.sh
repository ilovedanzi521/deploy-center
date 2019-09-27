#!/bin/sh
CC_FTP_SERVER=192.168.0.70
CC_FTP_PORT=21
CC_FTP_ROOT=/pub/repo
CC_FTP_USER=ftp
CC_FTP_PSWD=ftp
export CC_FTP_SERVER CC_FTP_PORT CC_FTP_ROOT CC_FTP_USER CC_FTP_PSWD

CC_HOME_DIR=/root/repo
CC_PACKAGES_DIR=/root/repo/packages
CC_PACKAGES_TOOL_DIR=/root/repo/packages/tools
CC_MODULES_DIR=/root/repo/modules
CC_SCRIPTS_DIR=/root/repo/scripts
export CC_HOME_DIR CC_MODULES_DIR CC_SCRIPTS_DIR

CC_DEPLOY_DIR=/root/deploy
CC_BACKUP_DIR=/root/deploy/backup
CC_LOG_DIR=/root/deploy/logs
export CC_DEPLOY_DIR CC_LOG_DIR CC_BACKUP_DIR

source /root/repo/common.sh

