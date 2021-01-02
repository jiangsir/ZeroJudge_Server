#checkout HEAD
import os
import fnmatch

# path=$(cd "$(dirname "$0")"; pwd)
# echo $path
path = os.path.dirname(os.path.realpath(__file__))

for file in os.listdir('/etc/init.d/'):
    if fnmatch.fnmatch(file, 'tomcat*'):
        tomcatN = file


print("path=" + path)
os.system('sudo apt-get install sudo')
os.system('sudo apt-get install ntpdate')
os.system('sudo apt-get install dos2unix')
os.system('sudo apt-get install rsync')
os.system('sudo apt-get install default-jdk')
os.system('sudo apt-get install fpc')
os.system('sudo apt-get install g++')

os.system('sudo cat /dev/null > /var/log/' + tomcatN + '/catalina.out')
# os.system('sudo find /etc/init.d/ -name "tomcat*" -exec bash -c "{} restart" \;')
os.system('sudo /etc/init.d/' + tomcatN + ' restart')

os.system('sudo rm ' + os.path.realpath(__file__))
