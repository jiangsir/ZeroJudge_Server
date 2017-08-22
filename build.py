#!/usr/bin/python3
import os
import fnmatch
import time
import subprocess
import datetime
import sys
from bs4 import BeautifulSoup

basedir = os.path.split(os.path.realpath(__file__))[0]
appname = sys.argv[1]
########################################################################################################

def os_exec(cmd):
    print('cmd=' + cmd)
    os.system(cmd)


########################################################################################################################
########################################################################################################################
for file in os.listdir('/etc/init.d/'):
    if fnmatch.fnmatch(file, 'tomcat*'):
        tomcatN = file



os_exec('/etc/init.d/' + tomcatN + ' restart')
os_exec('sudo rm -rf /var/lib/' + tomcatN + '/webapps/' + appname + '/')
os_exec('sudo cp ' + basedir + '/' + appname + '.war /var/lib/' + tomcatN + '/webapps/' + appname + '.war')
contextpath = '/var/lib/' + tomcatN + '/webapps/' + appname + '/META-INF/context.xml'
print("Waiting", end="")
while not os.path.isfile(contextpath):
    print(".", end="")
    time.sleep(1)
print()

