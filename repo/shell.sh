#!/bin/sh

execute() {
  cmdStr=$@
  result=`${cmdStr} 2>/dev/null`
  code=$?
  echo $code
  if [ $code -eq 0 ];then
    echo $result
  fi
}

case $1 in
  *)
	execute $@
	;;
esac

