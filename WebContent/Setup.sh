#!/bin/sh

path=$(cd "$(dirname "$0")"; pwd)
echo $path
#chmod +x $path/*.sh
sudo find $path -name "*.sh" -exec chmod +x {} +
sudo find $path -name "*.exe" -exec chmod +x {} +
sudo find $path -name "*.class" -exec chmod +x {} +

#sudo find /etc/init.d/ -name "tomcat*" -printf "%f\n";

sudo find /etc/init.d/ -name "tomcat*" -exec bash -c "{} restart" \;
