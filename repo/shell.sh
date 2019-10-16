#!/bin/sh

execute() {
  cmdStr=$@
  result=`${cmdStr} 2>/dev/null`
  code=$?
  echo $result
  if [ $code -eq 0 ];then
    echo "OK"
  fi
}

case $1 in
  *)
	execute $@
	;;
esac

