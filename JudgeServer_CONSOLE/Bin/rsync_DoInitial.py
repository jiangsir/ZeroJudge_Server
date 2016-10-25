#!/usr/bin/python3
import sys
import os

if(len(sys.argv) != 2):
    print("請輸入參數：$S")
    exit()

source = sys.argv[1]
lxc_path = "/var/lib/lxc/lxc-ALL/rootfs/"

print("IN LXC start")
print(sys.argv[0] + " | " + sys.argv[1])
rsync_CONSOLE = "rsync -av /JudgeServer_CONSOLE " + lxc_path
print(rsync_CONSOLE)
os.system(rsync_CONSOLE)

rsync_source = "rsync -av " + source + " " + lxc_path 
print(rsync_source)
os.system(rsync_source)

chown = "chown -R zero:zero " + lxc_path + source
print(chown)
os.system(chown)

print("IN LXC end")
