/*
* File: phase1.cpp 
* Designed for: 
* �����͵{�����D�t�� ZeroJudge(http://cat.nknush.kh.edu.tw/ZeroJudge/)
* Lastest Modified: May, 2008
* Author: Jiangsir (jiangsir@tea.nknush.kh.edu.tw)
*/

#include <iostream>
#include <sstream>
#include <sys/wait.h>
#include <cstdlib>

using namespace std;

int main(int argc, char *argv[]){
    int basestatus, status;
    rusage baseusage, usage;
    long long int time, memory, outfilesize;
//    stringstream ss;
//    ss << argv[1];
//    ss >> time;
//    ss << argv[2];
//    ss >> memory;
//    ss << argv[3];
//    ss >> outfilesize;
    stringstream(argv[1]) >> time;
    stringstream(argv[2]) >> memory;
    stringstream(argv[3]) >> outfilesize;

//    pid_t baseid = fork();
    if(fork()==0){
        rlimit timelimit, memorylimit;
        timelimit.rlim_cur = 1; // seconds
        timelimit.rlim_max = 1;
        memorylimit.rlim_cur = memory; // ��� Byte
        memorylimit.rlim_max = memory;
        setrlimit(RLIMIT_CPU, &timelimit);
        setrlimit(RLIMIT_AS, &memorylimit);
	system(argv[4]);
	return 0;
    }
    wait3(&basestatus, 0, &baseusage);

//    pid_t childid = fork();
    if(fork()==0){
        rlimit timelimit, memorylimit, filelimit;
	timelimit.rlim_cur = time; // seconds
	timelimit.rlim_max = time;
	memorylimit.rlim_cur = memory; // ��� Byte
	memorylimit.rlim_max = memory;
	filelimit.rlim_cur = outfilesize; // ��� Byte
	filelimit.rlim_max = outfilesize;
        setrlimit(RLIMIT_CPU, &timelimit);
	setrlimit(RLIMIT_AS, &memorylimit);
	setrlimit(RLIMIT_FSIZE, &filelimit);
        int childstatus = system(argv[5]);
	cout << "childstatus=" << childstatus << endl;
        cout << "WIFSIGNALED=" << WIFSIGNALED(childstatus) << endl; // �D�s�N��]�Y�� signal �S�� catch �ӵ���
        cout << "WTERMSIG=" << WTERMSIG(childstatus) << endl; // �y����{������ signal �s��
        cout << "WEXITSTATUS=" << WEXITSTATUS(childstatus) << endl;  // ���o exit status �̤p 8 bits
        cout << "WIFEXITED=" << WIFEXITED(childstatus) << endl; // �D�s�N���`����
        cout << "WCOREDUMP=" << WCOREDUMP(childstatus) << endl; // �D�s�N���� core dump
        cout << "WSTOPSIG=" << WSTOPSIG(childstatus) << endl; // ���o�y����{ stop �� signal
        cout << "WIFSTOPPED=" << WIFSTOPPED(childstatus) << endl; // �D�s�N��l��{�w�Q stop
        return 0;
    }
    wait3(&status, 0, &usage);

    double exectime = usage.ru_utime.tv_sec + usage.ru_stime.tv_sec + (double)(usage.ru_utime.tv_usec + 
usage.ru_stime.tv_usec) / 1000000;
    double basetime = baseusage.ru_utime.tv_sec + baseusage.ru_stime.tv_sec + (double)(baseusage.ru_utime.tv_usec +
baseusage.ru_stime.tv_usec) / 1000000;
    cout << "basetime=" << basetime << endl;
    cout << "time=" << exectime << endl;
    cout << "basemem=" << baseusage.ru_minflt << endl;
    cout << "mem=" << usage.ru_minflt << endl;
    cout << "pagesize=" << getpagesize() << endl;
    cout << "ru_majflt=" << usage.ru_majflt << endl;
    cout << "pid=" << getpid() << endl;
    cout << "ppid=" << getppid() << endl;

    return 0;
}
