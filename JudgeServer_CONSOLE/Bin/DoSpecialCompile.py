#!/usr/bin/python3
import sys
import os, subprocess

def run_cmd(cmd):
    print('run_cmd=', cmd)
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


if(len(sys.argv) != 5):
    print("請輸入參數：source problemid path_CONSOLE lxc_PATH")
    exit()

source = sys.argv[1]
problemid = sys.argv[2]
path_CONSOLE = sys.argv[3]
lxc_PATH = sys.argv[4]
path_Testdata = path_CONSOLE + "/Testdata"
path_Special = path_CONSOLE + "/Special"
path_Bin = path_CONSOLE + "/Bin"

# lxc
#lxc_path = "/var/lib/lxc/" + lxc_NAME + "/rootfs/"
# lxd 3.0.3
#lxc_path = "/var/lib/lxd/storage-pools/default/containers/"+lxc_NAME+"/rootfs/"

print("IN LXC start")
print(sys.argv[0] + " | " + sys.argv[1])

rsync_Testdata = "rsync -avR --delete --chmod=D755,F444 " + path_Testdata + "/" + problemid + " " + lxc_PATH
run_cmd(rsync_Testdata)

rsync_Special = "rsync -avR " + path_Special + "/" + problemid + " " + lxc_PATH
run_cmd(rsync_Special)

rsync_CONSOLE = "rsync -avR --delete --chmod=D755,F400 --exclude=" + path_Testdata + " " + path_CONSOLE + " " + lxc_PATH
run_cmd(rsync_CONSOLE)

rsync_source = "rsync -avR " + source + " " + lxc_PATH 
run_cmd(rsync_source)

chown = "chown -R nobody:nogroup " + lxc_PATH + source
run_cmd(chown)
chown = "chown -R nobody:nogroup " + lxc_PATH + path_Special + "/" + problemid
run_cmd(chown)


chown = "chown nobody:nogroup " + lxc_PATH + path_Bin + "/*.exe"
run_cmd(chown)
chown = "chmod +x " + lxc_PATH + path_Bin + "/*.exe"
run_cmd(chown)

print("IN LXC end")
