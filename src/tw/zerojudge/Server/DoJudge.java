/**
 * idv.jiangsir.Server - DoJudge.java
 * 2011/7/25 下午10:40:06
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;
import java.util.logging.Logger;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Factories.ServerFactory;
import tw.zerojudge.Server.Object.*;
import tw.zerojudge.Server.Object.Compiler;

/**
 * @author jiangsir
 * 
 */
public class DoJudge implements Runnable {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ServerInput serverInput;
	ServerOutput[] serverOutputs = null;
	Logger logger = Logger.getAnonymousLogger();

	public DoJudge(ServerInput serverInput) {
		this.serverInput = serverInput;
		serverOutputs = new ServerOutput[serverInput.getTestfiles().length];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		double[] timelimits = serverInput.getTimelimits();
		int[] scores = serverInput.getScores();
		// 每次評分都重新啟動 lxc
		// String lxc_restart = "sudo lxc-stop -n " + ServerConfig.lxc_NAME + ";sudo
		// lxc-start -n "
		// + ServerConfig.lxc_NAME;
		// 20200930 為維護效率，取消每次都 lxc restart
		//// String lxc_restart = "sudo lxc restart --all";
		//// new RunCommand(lxc_restart).run();

		try {
			new DoInitial(serverInput).run();
		} catch (JudgeException e) {
			e.printStackTrace();
			InitialOutput output = (InitialOutput) e.getCause();
			serverOutputs[0] = ServerFactory.newServerOutputFromInput(serverInput);
			serverOutputs[0].setJudgement(output.getJudgement());
			serverOutputs[0].setReason(output.getReason());
			serverOutputs[0].setHint(output.getHint());
			serverOutputs[0].setDebug(output.getDebug());
			return;
		}
		try {
			new DoCompile(serverInput).run();
		} catch (JudgeException e) {
			e.printStackTrace();
			CompileOutput compileOutput = (CompileOutput) e.getCause();
			serverOutputs[0] = ServerFactory.newServerOutputFromInput(serverInput);
			serverOutputs[0].setJudgement(compileOutput.getJudgement());
			serverOutputs[0].setInfo(compileOutput.getInfo());
			serverOutputs[0].setReason(compileOutput.getReason());
			serverOutputs[0].setHint(compileOutput.getHint());
			serverOutputs[0].setDebug(compileOutput.getDebug());
			serverOutputs[0].setScore(0);
			return;
		}
		try {
			new NameMangling(serverInput).run();
		} catch (JudgeException e) {
			e.printStackTrace();
			NameManglingOutput nmOutput = (NameManglingOutput) e.getCause();
			serverOutputs[0] = ServerFactory.newServerOutputFromInput(serverInput);
			serverOutputs[0].setJudgement(nmOutput.getJudgement());
			serverOutputs[0].setInfo(nmOutput.getInfo());
			serverOutputs[0].setReason(nmOutput.getReason());
			serverOutputs[0].setHint(nmOutput.getHint());
			serverOutputs[0].setDebug(nmOutput.getDebug());
			return;
		}
		for (int i = 0; i < serverInput.getTestfiles().length; i++) {
			System.out.println("xxxx serverInput.getTestfiles().length=" + serverInput.getTestfiles().length);
			System.out.println("xxxx serverOutputs.length=" + serverOutputs.length);
			serverOutputs[i] = ServerFactory.newServerOutputFromInput(serverInput);
			System.out.println("xxxx serverOutputs.length=" + serverOutputs.length);

			ExecuteInput executeInput = new ExecuteInput();
			executeInput.setCodename(serverInput.getCodename());
			String command = "";

			int outfilelimit = 100 * 1024 * 1024;
			System.out.println("xxxx[ timelimits.length=" + timelimits.length);
			double timelimit = timelimits[i];
			System.out.println("xxxx] timelimits.length=" + timelimits.length);

			Compiler compiler = serverInput.getCompiler();
			executeInput.setMemorylimit(serverInput.getMemorylimit());
			executeInput.setTimelimit(timelimit * compiler.getTimeextension());

			String cmd_execute = compiler.getCmd_execute();
			if (cmd_execute.contains("$S")) {
				cmd_execute = cmd_execute.replaceAll("\\$S", serverInput.getSource_TempPath().toString());
			}
			if (cmd_execute.contains("$C")) {
				cmd_execute = cmd_execute.replaceAll("\\$C", serverInput.getCodename());
			}
			if (cmd_execute.contains("$T")) {
				cmd_execute = cmd_execute.replaceAll("\\$T",
						serverConfig.getTestdataPath() + File.separator + serverInput.getTestfiles()[i] + ".in");
			}

			logger.info("cmd_execute=" + cmd_execute);
			String base = "base_";

			if (compiler.getLanguage().equals(ServerConfig.KNOWNED_LANGUAGE.C.name())
					|| compiler.getLanguage().equals(ServerConfig.KNOWNED_LANGUAGE.CPP.name())
					|| compiler.getLanguage().equals(ServerConfig.KNOWNED_LANGUAGE.PASCAL.name())
					|| compiler.getLanguage().equals(ServerConfig.KNOWNED_LANGUAGE.PYTHON.name())) {
				base = base + compiler.getSuffix() + ".exe";
			} else if (compiler.getLanguage().equals(ServerConfig.KNOWNED_LANGUAGE.JAVA.name())) {
				base = base + compiler.getSuffix();
			} else {
				base = base + ".exe";
			}
			if (compiler.getLanguage().equals(ServerConfig.KNOWNED_LANGUAGE.JAVA.name())) {
				command = serverConfig.getBinPath() + File.separator + "shell.exe "
						+ (int) Math.ceil(executeInput.getTimelimit()) + " "
						+ (executeInput.getMemorylimit() + serverConfig.getJVM_MB()) * 1024 * 1024 + " " + outfilelimit
						+ " \"java -classpath " + serverConfig.getBinPath() + " " + base + "\" \"" + cmd_execute
						+ "\" ";
			} else {
				command = serverConfig.getBinPath() + File.separator + "shell.exe "
						+ (int) Math.ceil(executeInput.getTimelimit()) + " "
						+ executeInput.getMemorylimit() * 1024 * 1024 + " " + outfilelimit + " \""
						+ serverConfig.getBinPath() + File.separator + base + "\" \"" + cmd_execute + "\" ";
			}

			executeInput.setCommand(command);
			executeInput.setCompiler(serverInput.getCompiler());
			executeInput.setSession_account(serverInput.getSession_account());
			executeInput.setTestfilename(serverInput.getTestfiles()[i]);

			ExecuteOutput executeOutput = null;
			try {
				executeOutput = new DoExecute(executeInput).run();
				serverOutputs[i].setTimeusage(executeOutput.getTimeusage());
				serverOutputs[i].setMemoryusage(executeOutput.getMemoryusage());
			} catch (JudgeException e) {
				e.printStackTrace();
				executeOutput = (ExecuteOutput) e.getCause();
				serverOutputs[i].setTimeusage(executeOutput.getTimeusage());
				serverOutputs[i].setMemoryusage(executeOutput.getMemoryusage());
				serverOutputs[i].setSession_account(serverInput.getSession_account());
				serverOutputs[i].setJudgement(executeOutput.getJudgement());
				serverOutputs[i].setInfo(executeOutput.getInfo());
				serverOutputs[i].setReason(executeOutput.getReason());
				serverOutputs[i].setHint(executeOutput.getHint());
				serverOutputs[i].setDebug(executeOutput.getDebug());

				// 此處只要在多測資下遇到一個 TLE 就結束。後面就不測了節省時間。
				if (serverConfig.getIsTLEbreak() && executeOutput.getJudgement() == ServerOutput.JUDGEMENT.TLE) {
					break;
				}
				// 市賽、教甄: 以上都要註解掉。也就是所有 TLE 都要做完。
				continue;
			}

			CompareInput compareInput = new CompareInput();
			compareInput.setCodename(serverInput.getCodename());
			compareInput.setMode(serverInput.getMode());
			compareInput.setProblemid(serverInput.getProblemid());
			compareInput.setSession_account(serverInput.getSession_account());
			compareInput.setShowdetail(serverInput.getErrmsg_visible());
			compareInput.setTimelimit(executeInput.getTimelimit());
			compareInput.setMemorylimit(executeInput.getMemorylimit());
			compareInput.setTimeusage(executeOutput.getTimeusage());
			compareInput.setMemoryusage(executeOutput.getMemoryusage());
			compareInput.setTestfilename(serverInput.getTestfiles()[i]);

			CompareOutput compareOutput = null;
			try {
				compareOutput = new DoCompare(serverInput, compareInput).run();
				serverOutputs[i].setJudgement(compareOutput.getJudgement());
				serverOutputs[i].setInfo(compareOutput.getInfo());
				serverOutputs[i].setReason(compareOutput.getReason());
				serverOutputs[i].setHint(compareOutput.getHint());
				serverOutputs[i].setScore(scores[i]);
				serverOutputs[i].setTimeusage(compareOutput.getTimeusage());
				serverOutputs[i].setMemoryusage(compareOutput.getMemoryusage());
			} catch (JudgeException e) {
				e.printStackTrace();
				compareOutput = (CompareOutput) e.getCause();
				serverOutputs[i].setJudgement(compareOutput.getJudgement());
				serverOutputs[i].setInfo(compareOutput.getInfo());
				serverOutputs[i].setReason(compareOutput.getReason());
				serverOutputs[i].setHint(compareOutput.getHint());
				serverOutputs[i].setTimeusage(compareOutput.getTimeusage());
				serverOutputs[i].setMemoryusage(compareOutput.getMemoryusage());
				continue;
			}

		}
		if (serverConfig.getIsCleanTmpFile()) {
			new DoClean(serverInput).run();
		}
	}

	public ServerOutput[] getServerOutputs() {
		return serverOutputs;
	}
}
