#!/usr/bin/python3
import sys
import os

if(len(sys.argv) == 1):
    print("請輸入參數：C $S")
    exit()

language = sys.argv[1]
print("language=" + language)
source = sys.argv[2]

lxc_path = "/var/lib/lxc/lxc-" + language + "/rootfs/";

rsync_CONSOLE = "rsync -av /JudgeServer_CONSOLE " + lxc_path
print(rsync_CONSOLE)
os.system(rsync_CONSOLE)

rsync_source = "rsync -av " + source + " " + lxc_path + "tmp/"
os.system(rsync_source)