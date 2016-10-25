#!/usr/bin/python3
import sys
import os

if(len(sys.argv) != 3):
    print("請輸入參數：source problemid")
    exit()

source = sys.argv[1]
problemid = sys.argv[2]
path_Testdata = "/JudgeServer_CONSOLE/Testdata/" + problemid

lxc_path = "/var/lib/lxc/lxc-ALL/rootfs/"

print("IN LXC start")
print(sys.argv[0] + " | " + sys.argv[1])

rsync_Testdata = "rsync -avR " + path_Testdata + " " + lxc_path
print(rsync_Testdata)
os.system(rsync_Testdata)

rsync_CONSOLE = "rsync -avR --exclude=" + path_Testdata + " /JudgeServer_CONSOLE " + lxc_path
print(rsync_CONSOLE)
os.system(rsync_CONSOLE)

rsync_source = "rsync -avR " + source + " " + lxc_path 
print(rsync_source)
os.system(rsync_source)

chown = "chown -R nobody:nogroup " + lxc_path + source
print(chown)
os.system(chown)

print("IN LXC end")
