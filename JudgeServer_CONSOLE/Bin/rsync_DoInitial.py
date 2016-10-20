#!/usr/bin/python3
import sys
import os

if(len(sys.argv) != 2):
    print("請輸入參數：$S")
    exit()

source = sys.argv[1]
lxc_path = "/var/lib/lxc/lxc-ALL/rootfs/"

rsync_CONSOLE = "rsync -av /JudgeServer_CONSOLE " + lxc_path
print(rsync_CONSOLE)
os.system(rsync_CONSOLE)

rsync_source = "rsync -av " + source + " " + lxc_path + "tmp/"
print(rsync_source)
os.system(rsync_source)