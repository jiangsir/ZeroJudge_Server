package tw.zerojudge.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
	private RunnableCause watchcause;

	public int exitCode = -1;
	private double timelimit = 0;
	public boolean isInterrupted = false;
	Logger logger = Logger.getAnonymousLogger();

	public RunCommand(String[] command, long delay) {
		this.command = command;
		this.delay = delay;
	}

	public RunCommand(String[] command) {
		this.command = command;
		this.delay = 0;
	}

	public RunCommand(String command) {
		this.command = new String[]{"/bin/sh", "-c", command};
		this.delay = 0;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}
	/**
	 * 採用 processBuilder
	 * 
	 * @throws IOException
	 */
	public void start(String cmd) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if (this.isInterrupted) {
			return;
		}
		long starttime = System.currentTimeMillis();
		List<String> commandList = new ArrayList<String>();
		for (String s : cmd.split(" "))
			commandList.add(s);
		ProcessBuilder pb = new ProcessBuilder(commandList);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if (this.isInterrupted) {
			return;
		}

		long starttime = System.currentTimeMillis();
		Runtime rt = Runtime.getRuntime();
		Process process;
		try {
			logger.info("command=" + command[2]);
			process = rt.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("runtime exec IOException!!");
			return;
		}

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

		long stoptime = System.currentTimeMillis();
		this.executetime = (stoptime - starttime);
		InputStream stdin = process.getInputStream();
		InputStream stderr = process.getErrorStream();
		try {
			String s = null;
			LineNumberReader err = new LineNumberReader(new InputStreamReader(stderr));
			while ((s = err.readLine()) != null && this.errorStream.size() < 500) {
				this.errorStream.add(s);
			}
			System.out.println("errorString=" + this.getErrorString());
			err.close();
			LineNumberReader in = new LineNumberReader(new InputStreamReader(stdin));
			String outstring = null;
			while ((outstring = in.readLine()) != null && this.outputStream.size() < 500) {
				this.outputStream.add(outstring);
			}
			System.out.println("outputString=" + this.getOutputString());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("IOException: " + e.getLocalizedMessage());
			return;
		}

		if (process.exitValue() != 0) {
			cause.setResourceMessage(ServerOutput.REASON.SYSTEMERROR.toString());
			cause.setPlainMessage("程序已被終止！ exit code=" + this.exitCode + ", exitValue=" + process.exitValue()
					+ ", errorString=" + this.getErrorString());
			cause.setExitCode(process.exitValue());
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
		// ArrayList<String> errorStream = new ArrayList<String>();
		// Iterator<String> it = this.errorStream.iterator();
		// while (it.hasNext()) {
		// errorStream.add(this.filter(it.next()));
		// }
		return errorStream;
	}

	public ArrayList<String> getOutputStream() {
		// ArrayList<String> outputStream = new ArrayList<String>();
		// Iterator<String> it = this.outputStream.iterator();
		// while (it.hasNext()) {
		// outputStream.add(this.filter(it.next()));
		// }
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

	public RunnableCause getWatchCause() {
		return watchcause;
	}

}
