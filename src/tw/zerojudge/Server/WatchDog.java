package tw.zerojudge.Server;

//import idv.jiangsir.Exceptions.GeneralCause;
import tw.zerojudge.Server.Exceptions.RunnableCause;

public class WatchDog implements Runnable {
	Process process;
	double max_timelimit = 40; // 任何 RunCommand 執行的程式都不得超過 35 秒
	double timelimit = 0; // 20100526 改以 s 為單位, 接受小數點
	boolean isFinish = false;
	private RunnableCause cause = null;

	public WatchDog(Process process) {
		this.process = process;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}

	public void run() {
		try {
			// 比最大時限再增加 1s
			if (this.timelimit != 0) {
				this.max_timelimit = this.timelimit + 10;
			}
			Thread.sleep((long) (this.max_timelimit * 1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("watch process thread 結束");
			e.printStackTrace();
			return;
		}
		if (!this.isFinish) {
			this.process.destroy();
			cause = new RunnableCause();
			cause.setResourceMessage(RunnableCause.Resource_PROCESS_DESTROIED);
			cause.setPlainMessage("process 沒有結束，因此強迫結束 do process.destroy(), "
					+ "watch 經過 " + this.max_timelimit + " s 啟動，強制結束");
			System.out.println("exitCode=" + cause.getExitCode() + ", message="
					+ cause.getPlainMessage());
			return;
		}
	}

	public void setFinished() {
		this.isFinish = true;
		// process.destroy(); // 不能在這裡 destroy , 否則 errStream 就抓不到了。
		Thread.interrupted();
	}

	public RunnableCause getCause() {
		return cause;
	}

}
