#!/usr/bin/python3
import sys
import os, subprocess

def run_cmd(cmd):
    print('doCompile.py RUN_CMD=', cmd)
    try:
        completed = subprocess.run(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    except subprocess.CalledProcessError as err:
        print('ERROR:', err)
    else:
        print('returncode:', completed.returncode)
        print('STDOUT: {!r}'.format(len(completed.stdout)),
              completed.stdout.decode('utf-8'))
        print('STDERR: {!r}'.format(len(completed.stderr)),
              completed.stderr.decode('utf-8'))


if(len(sys.argv) != 7):
    print("請輸入參數：source problemid path_CONSOLE lxc_NAME judgemode priority")
    exit()

source = sys.argv[1]
problemid = sys.argv[2]
path_CONSOLE = sys.argv[3]
lxc_PATH = sys.argv[4] 
judgemode = sys.argv[5]
priority = sys.argv[6]

path_Testdata = path_CONSOLE + "/Testdata"
path_Bin = path_CONSOLE + "/Bin"
path_Special = path_CONSOLE + "/Special"

# lxc
#lxc_path = "/var/lib/lxc/" + lxc_NAME + "/rootfs/"

# lxd 3.0.3
#lxc_path = "/var/lib/lxd/storage-pools/default/containers/"+lxc_NAME+"/rootfs/"


print("IN LXC start")
print(sys.argv[0] + " | " + sys.argv[1])
chmod = 'D755,F444'

if judgemode =='Special' or priority=='Testjudge':
#if problemid.startswith('Testjudge'):
    rsync_Testdata = "rsync -avR --delete --chmod="+chmod+" " + path_Testdata + "/" + problemid + " " + lxc_PATH
else:
    rsync_Testdata = "rsync -avR --delete --chmod="+chmod+" " + path_Testdata + "/" + problemid + "/*.in " + lxc_PATH
run_cmd(rsync_Testdata)

#--chown=nobody:nogroup
rsync_CONSOLE = "rsync -avR --delete --chmod=D755,F400 --exclude=" + path_Testdata + " " + path_CONSOLE + " " + lxc_PATH
run_cmd(rsync_CONSOLE)

rsync_source = "rsync -avR --chown=nobody:nogroup --chmod=D777,F777 " + source + " " + lxc_PATH
run_cmd(rsync_source)

chown = "chown nobody:nogroup " + lxc_PATH + path_Bin + "/*.exe"
run_cmd(chown)
chown = "chmod a+x " + lxc_PATH + path_Bin + "/*.exe"
run_cmd(chown)

chown = "chmod a+r " + lxc_PATH + path_Testdata + "/" + problemid+ "/*"
run_cmd(chown)

chown = "chmod -R 755 " + lxc_PATH + path_Special + "/" + problemid
run_cmd(chown)
chown = "chown -R nobody:nogroup " + lxc_PATH + path_Special + "/" + problemid
run_cmd(chown)

chown = "chown -R nobody:nogroup " + lxc_PATH + source
run_cmd(chown)

print("IN LXC end")
