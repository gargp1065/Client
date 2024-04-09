#!/bin/bash
source ~/.bash_profile
set -x
VAR=""
build_path="${APP_HOME}/httpclient/"
build="eir-client-file-download.jar"
cd $build_path
status=`ps -ef | grep $build | grep java`
if [ "$status" != "$VAR" ]
then
 echo "Process Already Running"
else
 echo "Starting Process"

java -Dlog4j.configurationFile=./log4j2.xml -jar $build ./config.properties 1>/dev/null 2>/dev/null &

echo "Process Started"

fi