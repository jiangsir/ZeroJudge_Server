
import os


#path=$(cd "$(dirname "$0")"; pwd)
#echo $path
path = os.path.dirname( os.path.realpath( __file__ ) )

os.system('sudo apt-get install sudo')
os.system('sudo apt-get install ntpdate')
os.system('sudo apt-get install dos2unix')
os.system('sudo apt-get install rsync')
os.system('sudo apt-get install default-jdk')
os.system('sudo apt-get install g++')
os.system('sudo apt-get install fpc')

os.system('sudo find '+path+' -name "*.sh" -exec chmod +x {} +')
os.system('sudo find '+path+' -name "*.exe" -exec chmod +x {} +')
os.system('sudo find '+path+' -name "*.class" -exec chmod +x {} +')
os.system('sudo chmod a+w '+path+'/JudgeServer_CONSOLE/Testdata')
os.system('sudo chmod a+w '+path+'/JudgeServer_CONSOLE/Special')

os.system('sudo rm -rf /JudgeServer_CONSOLE')
os.system('sudo mv '+path+'/JudgeServer_CONSOLE /')
os.system('sudo g++ -o /JudgeServer_CONSOLE/Bin/shell.exe /JudgeServer_CONSOLE/Bin/shell.cpp')


os.system('sudo cat /dev/null > /var/log/tomcat8/catalina.out')
os.system('sudo find /etc/init.d/ -name "tomcat*" -exec bash -c "{} restart" \;')
os.system('sudo rm '+path+'/Setup.py')
