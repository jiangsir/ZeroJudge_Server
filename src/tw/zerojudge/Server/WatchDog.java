package tw.zerojudge.Server;

//import idv.jiangsir.Exceptions.GeneralCause;
import tw.zerojudge.Server.Exceptions.RunnableCause;

public class WatchDog implements Runnable {
	Process process;
	double max_timelimit = 40; 
	double timelimit = 0; 
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
			if (this.timelimit != 0) {
				this.max_timelimit = this.timelimit + 10;
			}
			Thread.sleep((long) (this.max_timelimit * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		if (!this.isFinish) {
			this.process.destroy();
			cause = new RunnableCause();
			cause.setResourceMessage(RunnableCause.Resource_PROCESS_DESTROIED);
			cause.setPlainMessage("process 沒有結束，因此強迫結束 do process.destroy(), "
					+ "watch 經過 " + this.max_timelimit + " s 啟動，強制結束");
			return;
		}
	}

	public void setFinished() {
		this.isFinish = true;
		Thread.interrupted();
	}

	public RunnableCause getCause() {
		return cause;
	}

}
