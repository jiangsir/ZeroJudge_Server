#!/usr/bin/python3
import sys
import subprocess
import os


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


if(len(sys.argv) != 4):
    print("請輸入參數：lxc_NAME tmppath outfile")
    exit()

lxc_NAME = sys.argv[1]
tmppath = sys.argv[2]
outfile = sys.argv[3]
lxc_path = "/var/lib/lxc/" + lxc_NAME + "/rootfs/"
print("DoCompare.py start")

outfilepath = os.path.join(lxc_path + tmppath, outfile)

run_cmd('whoami')

scp_outfile = "scp " + outfilepath + " " + tmppath
run_cmd(scp_outfile)

#chmod = "chmod +r " + os.path.join(tmppath, outfile)
# run_cmd(chmod)

os.chmod(os.path.join(tmppath, outfile), 0o644)

print("DoCompare.py END")
