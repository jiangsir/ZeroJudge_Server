#!/usr/bin/python3
import os
import fnmatch
import time
import subprocess
import datetime
import sys
import fire
from bs4 import BeautifulSoup

for file in os.listdir('/etc/init.d/'):
    if fnmatch.fnmatch(file, 'tomcat*'):
        tomcatN = file

basedir = os.path.split(os.path.realpath(__file__))[0]
webappspath = '/var/lib/' + tomcatN + '/webapps/'

########################################################################################################


class ZeroJudgeServerBuild(object):
    '''

    '''

    def os_exec(self, cmd):
        print('cmd=' + cmd)
        os.system(cmd)

    def build(self, warname):
        #self.os_exec('/etc/init.d/' + tomcatN + ' restart')

        self.os_exec('sudo rm -rf /var/lib/' + tomcatN +
                     '/webapps/' + warname + '/')
        self.os_exec('sudo cp ' + basedir + '/' + warname +
                     '.war ' + webappspath + warname + '.war')
        contextpath = webappspath + warname + '/META-INF/context.xml'
        print("Waiting", end="", flush=True)
        while not os.path.isfile(contextpath):
            print(".", end="", flush=True)
            time.sleep(1)
        print()


if __name__ == '__main__':
    fire.Fire(ZeroJudgeServerBuild)
