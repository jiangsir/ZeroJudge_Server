# TODO LIST 集中到 ZeroJudge


---------------------------------------------
### Commit: Testjudge
* FIXED_還是要處理 testdata 只應 rsync *.in 就好。
  只要 rsync *.in 就好，但要確保 Testjudge 也可以用。

### Commit: rsync Testjudge
* 在 doCompile.py 中判斷是 Testjudge 則全部同步，若是 problemid 則只同步 *.in

### Commit: fix testjudge
* 回復 exclude out 的修改
    exclude out 還有問題，這樣改會導致 testjudge 測資檔無法建立出來。先改回來。

### Commit: exclude out
* solutionid#4586435 利用 python open 直接讀取 .out 答案檔
    f=open("/JudgeServer_CONSOLE/Testdata/e036/e036.out","r")
    for i in f.readlines():
        print(i)
* DONE_FIX_20190317 doCompile.py 僅把 *.in rsync 進入 LXC 的 Testdata 以避免使用者

### Commit TODOLIST
* DONE_FIX_20190314 Special Judge 沒有足夠權限編譯出 .exe 問題出在 /JudgeServer_CONSOLE/Special/b053 這個資料夾沒有開啟 w 權限。已修正
### Commit
* DONE_FIX_20190314 Speical Judge 多測資的第一個正確，但第二個之後都會錯。shell.exe permission deny. 修改 DoSpecialCompile.py 在 rsync 時加上
--delete --chmod=D511,F444 改權限

#Tag: ZeroJudge_Server 3.2.1
* DONE_FIX_20190227 通告有漏洞，用 python os.system() 能夠執行系統指令
* DONE_修復 os.system 漏洞，在 LXC 內部的 /JudgeServer_CONSOLE 下手，執行程式的權限改成 nobody，相應的就有比如 
Bin/*.exe 需要一起改成 nobody


## HEAD: ZeroJudge_Server 3.2.0_beta_branch
* DONE_修復 os.system 漏洞，在 LXC 內部的 /JudgeServer_CONSOLE 下手，執行程式的權限改成 nobody，相應的就有比如 
Bin/*.exe 需要一起改成 nobody

# Tag: ZeroJudge_Server 3.2.0_build0129


