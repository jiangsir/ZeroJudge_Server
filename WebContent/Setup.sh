#!/bin/sh

path=$(cd "$(dirname "$0")"; pwd)
echo $path

sudo apt-get install sudo
sudo apt-get install ntpdate
sudo apt-get install dos2unix
sudo apt-get install rsync
sudo apt-get install default-jdk
sudo apt-get install g++
sudo apt-get install fpc

sudo find $path -name "*.sh" -exec chmod +x {} +
sudo find $path -name "*.exe" -exec chmod +x {} +
sudo find $path -name "*.class" -exec chmod +x {} +
sudo chmod a+w $path/JudgeServer_CONSOLE/Testdata
sudo chmod a+w $path/JudgeServer_CONSOLE/Special

sudo rm -rf /JudgeServer_CONSOLE
sudo mv $path/JudgeServer_CONSOLE /
sudo g++ -o /JudgeServer_CONSOLE/Bin/shell.exe /JudgeServer_CONSOLE/Bin/shell.cpp

sudo find /etc/init.d/ -name "tomcat*" -exec bash -c "{} restart" \;

sudo rm $path/Setup.sh
