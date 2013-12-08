package tw.zerojudge.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.RunnableCause;

public class RunCommand implements Runnable {

	private String[] command;
	private long delay = 0;
	public long executetime;

	private ArrayList<String> errorStream = new ArrayList<String>();
	private ArrayList<String> outputStream = new ArrayList<String>();
	private RunnableCause cause = new RunnableCause();

	public int exitCode = -1;
	private double timelimit = 0; // 改以 以 s 為單位, 允許小數點。
	public boolean isInterrupted = false;

	// public LineNumberReader in = null;

	public RunCommand(String[] command, long delay) {
		this.command = command;
		this.delay = delay;
	}

	public RunCommand(String[] command) {
		this.command = command;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}

	public void run() {
		// String key = String.valueOf(new Date().getTime());
		// ENV.ThreadPool.put(key, Thread.currentThread());
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if (this.isInterrupted) {
			System.out.println("command 被中斷: " + command[2]);
			return;
		}

		long starttime = new Date().getTime();
		Runtime rt = Runtime.getRuntime();
		Process process;
		try {
			process = rt.exec(command);
		} catch (IOException e1) {
			e1.printStackTrace();
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("runtime exec IOException!!");
			return;
		}

		RunnableCause watchcause;
		try {
			WatchDog watch = new WatchDog(process);
			if (this.timelimit != 0) {
				watch.setTimelimit(this.timelimit);
			}
			new Thread(watch).start();
			this.exitCode = process.waitFor();
			watch.setFinished();
			watchcause = watch.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("Interrupted Exception!!");
			return;
		}

		if (watchcause != null) {
			watchcause.setResourceMessage(watchcause.getResourceMessage());
			watchcause.setPlainMessage(watchcause.getPlainMessage());
			watchcause.setExitCode(watchcause.getExitCode());
			return;
		}
		if (process.exitValue() != 0) {
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("程序已被終止！ exit code=" + this.exitCode
					+ ", exitValue=" + process.exitValue());
			cause.setExitCode(process.exitValue());
			return;
		}
		long stoptime = new Date().getTime();
		this.executetime = (stoptime - starttime);
		InputStream stdin = process.getInputStream(); // qx 取得該指令行的正常輸出
		InputStream stderr = process.getErrorStream(); // qx 取得該指令行的錯誤輸出

		try {
			String s = null;
			LineNumberReader err = new LineNumberReader(new InputStreamReader(
					stderr));
			while ((s = err.readLine()) != null
					&& this.errorStream.size() < 500) {
				this.errorStream.add(s);
			}
			err.close();
			LineNumberReader in = new LineNumberReader(new InputStreamReader(
					stdin));
			String outstring = null;
			while ((outstring = in.readLine()) != null
					&& this.outputStream.size() < 500) {
				this.outputStream.add(outstring);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("IOException: " + e.getLocalizedMessage());
			return;
		}
	}

	public String getThreadName() {
		return Thread.currentThread().getName();
	}

	public boolean isThreadActive() {
		return Thread.currentThread().isAlive();
	}

	public void interrupt() {
		this.isInterrupted = true;
		Thread.currentThread().interrupt();
		// qx 底下是不會中斷的, 要自己在 run 內指定 flag 才能中斷
		// qx 因為 Thread 一定要跑完 run() 才能結束
		// Thread.currentThread().interrupt();
		// System.out.println("這個 Thread 是否已停止 = "
		// + Thread.currentThread().isInterrupted());
	}

	/**
	 * 過濾掉一些主機路徑的訊息。
	 * 
	 * @return
	 */
	private String filter(String s) {
		ServerConfig serverConfig = ConfigFactory.getServerConfig();
		if (s.contains(serverConfig.getTestdataPath().getPath())) {
			s = s.replaceAll(serverConfig.getTestdataPath().getPath(), "");
		}
		if (s.contains(serverConfig.getTempPath().getPath())) {
			s = s.replaceAll(serverConfig.getTempPath().getPath(), "");
		}
		return s;
	}

	public ArrayList<String> getErrorStream() {
		ArrayList<String> errorStream = new ArrayList<String>();
		Iterator<String> it = this.errorStream.iterator();
		while (it.hasNext()) {
			errorStream.add(this.filter(it.next()));
		}
		return errorStream;
	}

	public ArrayList<String> getOutputStream() {
		ArrayList<String> outputStream = new ArrayList<String>();
		Iterator<String> it = this.outputStream.iterator();
		while (it.hasNext()) {
			outputStream.add(this.filter(it.next()));
		}
		return outputStream;
	}

	public String getErrorString() {
		String errorString = "";
		for (int i = 0; i < this.errorStream.size(); i++) {
			if (errorString.length() < 5000) {
				errorString += this.filter(this.errorStream.get(i)) + "\n";
			} else {
				errorString += "錯誤訊息太長，到此截斷...";
				break;
			}
		}
		return errorString;
	}

	public String getOutputString() {
		String outputString = "";
		for (int i = 0; i < this.outputStream.size(); i++) {
			if (outputString.length() < 5000) {
				outputString += this.filter(this.outputStream.get(i)) + "\n";
			} else {
				outputString += "錯誤訊息太長，到此截斷...";
				break;
			}
		}
		return outputString;
	}

	public RunnableCause getCause() {
		return cause;
	}

}
