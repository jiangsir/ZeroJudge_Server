#!/bin/sh

path=$(cd "$(dirname "$0")"; pwd)
echo $path

apt-get install sudo
apt-get install ntpdate
apt-get install dos2unix
apt-get install rsync
apt-get install default-jdk
apt-get install g++
apt-get install fpc

sudo find $path -name "*.sh" -exec chmod +x {} +
sudo find $path -name "*.exe" -exec chmod +x {} +
sudo find $path -name "*.class" -exec chmod +x {} +
sudo chmod a+w $path/JudgeServer_CONSOLE/Testdata
sudo chmod a+w $path/JudgeServer_CONSOLE/Special

rm -rf /JudgeServer_CONSOLE
mv $path/JudgeServer_CONSOLE /

sudo find /etc/init.d/ -name "tomcat*" -exec bash -c "{} restart" \;

