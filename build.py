#!/usr/bin/python3
import os
import fnmatch
import time
import subprocess
import datetime
import sys
import fire
from bs4 import BeautifulSoup

basedir = os.path.split(os.path.realpath(__file__))[0]

########################################################################################################


class ZeroJudgeServerBuild(object):
    '''

    '''

    def os_exec(self, cmd):
        print('cmd=' + cmd)
        os.system(cmd)

    def build(self, warname, tomcatN='tomcat?'):
        #self.os_exec('/etc/init.d/' + tomcatN + ' restart')
        if tomcatN == 'tomcat?':
            for file in os.listdir('/etc/'):
                if 'tomcat' in file:
                    tomcatN = file
        webappspath = '/var/lib/' + tomcatN + '/webapps/'

        self.os_exec(f'sudo rm -rf /var/lib/{tomcatN}/webapps/{warname}/')
        self.os_exec(
            f'sudo cp {basedir}/{warname}.war {webappspath}{warname}.war')
        contextpath = webappspath + warname + '/META-INF/context.xml'
        print("Waiting", end="", flush=True)
        while not os.path.isfile(contextpath):
            print(".", end="", flush=True)
            time.sleep(1)
        print()


if __name__ == '__main__':
    fire.Fire(ZeroJudgeServerBuild)
