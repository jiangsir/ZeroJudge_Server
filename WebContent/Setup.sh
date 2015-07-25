#!/bin/sh

path=$(cd "$(dirname "$0")"; pwd)
echo $path

sudo find $path -name "*.sh" -exec chmod +x {} +
sudo find $path -name "*.exe" -exec chmod +x {} +
sudo find $path -name "*.class" -exec chmod +x {} +
sudo chmod a+w $path/JudgeServer_CONSOLE/Testdata
sudo chmod a+w $path/JudgeServer_CONSOLE/Special

rm -rf /JudgeServer_CONSOLE
mv $path/JudgeServer_CONSOLE /

sudo find /etc/init.d/ -name "tomcat*" -exec bash -c "{} restart" \;

