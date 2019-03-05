#!/usr/bin/python3
import sys
import os

if(len(sys.argv) != 5):
    print("請輸入參數：source problemid path_CONSOLE lxc_NAME")
    exit()

source = sys.argv[1]
problemid = sys.argv[2]
path_CONSOLE = sys.argv[3]
lxc_NAME = sys.argv[4]
path_Testdata = path_CONSOLE + "/Testdata"
path_Special = path_CONSOLE + "/Special"

lxc_path = "/var/lib/lxc/" + lxc_NAME + "/rootfs/"

print("IN LXC start")
print(sys.argv[0] + " | " + sys.argv[1])

rsync_Testdata = "rsync -avR " + path_Testdata + "/" + problemid + " " + lxc_path
print(rsync_Testdata)
os.system(rsync_Testdata)

rsync_Special = "rsync -avR " + path_Special + "/" + problemid + " " + lxc_path
print(rsync_Special)
os.system(rsync_Special)

rsync_CONSOLE = "rsync -avR --exclude=" + path_Testdata + " " + path_CONSOLE + " " + lxc_path
print(rsync_CONSOLE)
os.system(rsync_CONSOLE)

rsync_source = "rsync -avR " + source + " " + lxc_path 
print(rsync_source)
os.system(rsync_source)

chown = "chown -R nobody:nogroup " + lxc_path + source
print(chown)
os.system(chown)
chown = "chown -R nobody:nogroup " + lxc_path + path_Special + "/" + problemid
print(chown)
os.system(chown)

print("IN LXC end")
