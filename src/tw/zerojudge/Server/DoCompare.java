/**
 * idv.jiangsir.Server - DoCompare.java
 * 2011/7/25 下午10:42:54
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Configs.ServerConfig.KNOWNED_LANGUAGE;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.CompareInput;
import tw.zerojudge.Server.Object.CompareOutput;
import tw.zerojudge.Server.Object.CompileOutput;
import tw.zerojudge.Server.Object.ExecuteInput;
import tw.zerojudge.Server.Object.ExecuteOutput;

/**
 * @author jiangsir
 * 
 */
public class DoCompare {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ServerInput serverInput;
	CompareInput compareInput;
	CompareOutput compareOutput = new CompareOutput();

	public DoCompare(ServerInput serverInput, CompareInput compareInput) {
		this.serverInput = serverInput;
		this.compareInput = compareInput;
	}

	public CompareOutput run() throws JudgeException {
		BufferedReader systemout = null;
		BufferedReader userout = null;
		compareOutput.setTimeusage(compareInput.getTimeusage());
		compareOutput.setMemoryusage(compareInput.getMemoryusage());
		System.out.println("compareOutput.getTimeusage()=" + compareOutput.getTimeusage());
		// String lxc_path = "/var/lib/lxc/" + serverConfig.getLxc_NAME() + "/rootfs/";

		// String cmd_moveout = "sudo scp " + lxc_path
		// + new File(serverInput.getSource_TempPath(), compareInput.getCodename() +
		// ".out") + " "
		// + serverInput.getSource_TempPath().toString();
		// RunCommand runCommand = new RunCommand(cmd_moveout);
		// runCommand.run();

		new RunCommand("").start("sudo python3 " + serverConfig.getBinPath() + File.separator + "DoCompare.py "
				+ serverConfig.getLxc_PATH() + " " + serverInput.getSource_TempPath().toString() + " "
				+ compareInput.getCodename() + ".out");

		try {
			File testOutfile = new File(ConfigFactory.getServerConfig().getTestdataPath(),
					compareInput.getTestfilename() + ".out");
			FileInputStream system = new FileInputStream(testOutfile);
			// FileInputStream user = new FileInputStream(
			// new File(serverConfig.getTempPath(), compareInput.getCodename() +
			// ".out"));
			FileInputStream user = new FileInputStream(
					new File(serverConfig.getTempPath() + File.separator + serverInput.getSolutionid(),
							compareInput.getCodename() + ".out"));

			systemout = new BufferedReader(new InputStreamReader(system, "UTF-8"));
			userout = new BufferedReader(new InputStreamReader(user, "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
			compareOutput.setHint("未產生輸出檔。(" + e.getLocalizedMessage() + ")");
			throw new JudgeException(compareOutput);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
			compareOutput.setHint("編碼錯誤。 (UnsupportedEncodingException)");
			throw new JudgeException(compareOutput);
		}

		if (ServerInput.MODE.Tolerant == compareInput.getMode()) {
			compareOutput = this.TolerantComparison(systemout, userout);
			return compareOutput;
		} else if (ServerInput.MODE.Strictly == compareInput.getMode()) {
			compareOutput = this.StrictlyComparison(systemout, userout);
			return compareOutput;
		} else if (ServerInput.MODE.Special == compareInput.getMode()) {
			File useroutfile = new File(serverInput.getSource_TempPath(), compareInput.getCodename() + ".out");
			compareOutput = this.SpecialComparison(
					new File(serverConfig.getTestdataPath(), compareInput.getTestfilename() + ".in"),
					new File(serverConfig.getTestdataPath(), compareInput.getTestfilename() + ".out"), useroutfile);
			try {
				systemout.close();
				userout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return compareOutput;
		} else {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setHint("未知的 judge mode(" + compareInput.getMode() + "), 必須是 寬鬆 or 嚴格 or 特殊 其中之一！");
			try {
				systemout.close();
				userout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			throw new JudgeException(compareOutput);
		}
	}

	//

	private String parseLine(String line) {
		int max_length = 1000;
		if (line.length() <= max_length) {
			return line;
		} else {
			line = line.substring(0, max_length - 1);
			return line + "...訊息太長省略。";
		}
	}

	/**
	 * 寬鬆比對，掠過空行，並且 trim 處理。
	 */
	private CompareOutput TolerantComparison(BufferedReader systemout, BufferedReader userout) throws JudgeException {
		// CompareOutput compareOutput = new CompareOutput();
		int count = 0;
		String systemline = null;
		String userline = null;
		try {
			do {
				systemline = systemout.readLine();
				userline = userout.readLine();
				if ("".equals(userline) || "".equals(systemline)) {
					while ("".equals(userline)) {
						userline = userout.readLine();
					}
					while ("".equals(systemline)) {
						systemline = systemout.readLine();
					}
				}
				count++;
				if (userline == null || systemline == null) {
					break;
				}
				userline = userline.trim();
				systemline = systemline.trim();
				if (this.isEquals(count, userline, systemline)) {
					continue;
				} else {
					if (userline.length() > systemline.length() + 4) {
						userline = userline.substring(0, (int) (systemline.length() + 6) - 1) + " ...略";
					}
					compareOutput.setJudgement(ServerOutput.JUDGEMENT.WA);
					compareOutput.setReason(ServerOutput.REASON.ANSWER_NOT_MATCHED);
					compareOutput.setHint(
							"您的答案為: " + this.parseLine(userline) + "\n正確答案為: " + this.parseLine(systemline) + "\n");
					compareOutput.setInfo("line:" + count);
					throw new JudgeException(compareOutput);
				}
			} while (systemline != null && userline != null);
			userout.close();
			systemout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (userline == null && systemline != null) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.WA);
			compareOutput.setInfo("line:" + count);
			compareOutput.setReason(ServerOutput.REASON.LESS_THAN_STANDARD_OUTPUT);
			compareOutput.setHint("您共輸出 " + (count - 1) + " 行。");
			throw new JudgeException(compareOutput);
		} else if (userline != null && systemline == null) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.OLE);
			compareOutput.setInfo("line:" + count);
			compareOutput.setReason(ServerOutput.REASON.MORE_THAN_STANDARD_OUTPUT);
			compareOutput.setHint("請勿輸出題目未要求的文字: \n" + userline);
			throw new JudgeException(compareOutput);
		} else if (systemline == null && userline == null) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.AC);
			compareOutput.setReason(ServerOutput.REASON.AC);
			compareOutput.setHint("");
			return compareOutput;
		} else {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			compareOutput.setHint("寬鬆比對時發生未定義錯誤！");
			throw new JudgeException(compareOutput);
		}
	}

	/**
	 * 嚴格比對，就如同 UVa/ACM 一樣，只要有一個空格，空行不同就算錯誤。
	 */
	private CompareOutput StrictlyComparison(BufferedReader systemout, BufferedReader userout) throws JudgeException {
		int count = 0;
		String systemline = null;
		String userline = null;
		do {
			count++;
			try {
				systemline = systemout.readLine();
				userline = userout.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (userline == null || systemline == null) {
				break;
			}
			if (userline.equals(systemline)) {
				continue;
			} else {
				if (userline.length() > systemline.length() + 4) {
					userline = userline.substring(0, (int) (systemline.length() + 6) - 1) + " ...略";
				}

				compareOutput.setJudgement(ServerOutput.JUDGEMENT.WA);
				compareOutput.setReason(ServerOutput.REASON.ANSWER_NOT_MATCHED);
				compareOutput.setInfo("line:" + count);
				compareOutput.setHint(
						"您的答案為: " + this.parseLine(userline) + "\n正確答案為: " + this.parseLine(systemline) + "\n");
				throw new JudgeException(compareOutput);
			}
		} while (systemline != null && userline != null);

		if (userline == null && systemline != null) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.WA);
			compareOutput.setInfo("line:" + count);
			compareOutput.setReason(ServerOutput.REASON.LESS_THAN_STANDARD_OUTPUT);
			compareOutput.setHint("您只輸出了 " + (count - 1) + " 行。");
			throw new JudgeException(compareOutput);
		} else if (userline != null && systemline == null) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.OLE);
			compareOutput.setInfo("line:" + count);
			compareOutput.setReason(ServerOutput.REASON.MORE_THAN_STANDARD_OUTPUT);
			compareOutput.setHint("請勿輸出題目未要求的文字: \n" + userline);
			throw new JudgeException(compareOutput);
		} else if (systemline == null && userline == null) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.AC);
			compareOutput.setReason(ServerOutput.REASON.AC);
			compareOutput.setHint("");
			return compareOutput;
		} else {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			compareOutput.setHint("嚴格比對時發生未定義錯誤！");
			throw new JudgeException(compareOutput);
		}
	}

	// private void SpecialCompile(File judge_source, File judge_exe)
	// throws JudgeException {
	// CompareOutput output = new CompareOutput();
	// try {
	// new DoSpecialCompile(judge_source, judge_exe).run();
	// } catch (JudgeException e) {
	// e.printStackTrace();
	// CompileOutput compileOutput = (CompileOutput) e.getCause();
	// output.setJudgement(compileOutput.getJudgement());
	// output.setInfo(compileOutput.getInfo());
	// output.setReason(compileOutput.getReason());
	// output.setHint(compileOutput.getHint());
	// throw new JudgeException(output);
	// }
	// }

	/**
	 * 條件式評分，交由出題者自行決定
	 * 
	 * @param judger
	 * @param systeminfile
	 * @param systemoutfile
	 * @param useroutfile
	 * @return
	 */
	private CompareOutput SpecialComparison(File systeminfile, File systemoutfile, File useroutfile)
			throws JudgeException {
		Logger.getAnonymousLogger().info(
				"systeminfile=" + systeminfile + ", systemoutfile=" + systemoutfile + ", useroutfile=" + useroutfile);
		File SpecialPath = serverConfig.getSpecialPath(compareInput.getProblemid());
		Logger.getAnonymousLogger().info("SpecialPath=" + SpecialPath);
		if (SpecialPath == null || !SpecialPath.exists() || !SpecialPath.isDirectory()) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setReason(ServerOutput.REASON.SPECIAL_JUDGE_NOT_FOUND);
			compareOutput.setHint("Special Judge 資料夾不存在！ SpecialPath=" + SpecialPath);
			throw new JudgeException(compareOutput);
		}
		File[] SpecialJudgeFiles = SpecialPath.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("Special_" + compareInput.getProblemid());
			}
		});
		Logger.getAnonymousLogger().info("LINE1");
		if (SpecialJudgeFiles == null || SpecialJudgeFiles.length == 0) {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setReason(ServerOutput.REASON.SPECIAL_JUDGE_NOT_FOUND);
			compareOutput.setHint("Special Judge 資料夾內無正確檔案！");
			throw new JudgeException(compareOutput);
		}
		File special_source = null;
		Logger.getAnonymousLogger().info("SpecialJudgeFiles=" + SpecialJudgeFiles);
		for (File file : SpecialJudgeFiles) {
			Logger.getAnonymousLogger().info("SpecialJudgeFiles:file=" + file);
			if (special_source == null || FileUtils.isFileNewer(file, special_source.lastModified())) {
				special_source = file;
			}
		}
		Logger.getAnonymousLogger().info("special_source=" + special_source);

		String path = special_source.getPath();
		ServerConfig.KNOWNED_LANGUAGE special_language = ServerConfig.KNOWNED_LANGUAGE
				.valueOf(path.substring(path.lastIndexOf(".") + 1, path.length()).toUpperCase());
		Logger.getAnonymousLogger().info("LINE2");

		String SpecialJudge_exe = "";
		if (special_language == KNOWNED_LANGUAGE.CPP) {
			File special_exe = new File(special_source.toString().replaceAll(".cpp", ".exe"));
			if (!special_source.exists()) {
				compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
				compareOutput.setReason(ServerOutput.REASON.SPECIAL_JUDGE_NOT_FOUND);
				compareOutput.setHint("Special Judge 原始程式不存在！" + special_source);
				throw new JudgeException(compareOutput);
			} else if (!special_exe.exists()) {
				try {
					new DoSpecialCompile(special_source, special_exe, serverInput).run();
				} catch (JudgeException e) {
					e.printStackTrace();
					CompileOutput compileOutput = (CompileOutput) e.getCause();
					compareOutput.setJudgement(compileOutput.getJudgement());
					compareOutput.setInfo(compileOutput.getInfo());
					compareOutput.setReason(compileOutput.getReason());
					compareOutput.setHint(compileOutput.getHint());
					throw new JudgeException(compareOutput);
				}
			}
			SpecialJudge_exe = special_exe.getPath();
		} else if (special_language == KNOWNED_LANGUAGE.PYTHON) {
			SpecialJudge_exe = "python3 " + special_source;
		}
		Logger.getAnonymousLogger().info("LINE3");
		// 20181205 doCompare 取消 -u nobody
		// 20190409 取消 sudo
		String lxc_attach = serverConfig.getLxc_EXEC() + " -- ";
		String cmd_special = lxc_attach + " sudo -u nobody " + serverConfig.getBinPath() + File.separator + "shell.exe "
				+ (int) Math.ceil(compareInput.getTimelimit()) + " " + compareInput.getMemorylimit() * 1024 * 1024 + " "
				+ 100 * 1024 * 1024 + " \"" + serverConfig.getBinPath() + File.separator + "base_cpp.exe\" \""
				+ SpecialJudge_exe + " \"" + systeminfile + "\"" + " \"" + systemoutfile + "\"" + " \"" + useroutfile
				+ "\"" + "\"";
		ExecuteInput executeInput = new ExecuteInput();
		executeInput.setCommand(cmd_special);
		executeInput.setMemorylimit(compareInput.getMemorylimit());
		executeInput.setTimelimit(compareInput.getTimelimit());
		executeInput.setCodename(serverInput.getCodename());
		executeInput.setSession_account(serverInput.getSession_account());
		Logger.getAnonymousLogger().info("LINE4");

		try {
			ExecuteOutput executeOutput = new DoSpecialExecute(executeInput, serverInput).run();
			compareOutput.setJudgement(executeOutput.getJudgement());
			compareOutput.setInfo(executeOutput.getInfo());
			compareOutput.setReason(executeOutput.getReason());
			compareOutput.setHint(executeOutput.getHint());
			// 不應該在這裡更動 timeusage 否則會變成 Special Compare 的時間，而非真正執行時間。
			// compareOutput.setTimeusage(executeOutput.getTimeusage());
			// compareOutput.setMemoryusage(executeOutput.getMemoryusage());
			return compareOutput;
		} catch (JudgeException e) {
			e.printStackTrace();
			ExecuteOutput executeOutput = (ExecuteOutput) e.getCause();
			compareOutput.setJudgement(executeOutput.getJudgement());
			compareOutput.setInfo(executeOutput.getInfo());
			compareOutput.setReason(executeOutput.getReason());
			compareOutput.setHint(executeOutput.getHint());
			// 不應該在這裡更動 timeusage 否則會變成 Special Compare 的時間，而非真正執行時間。
//			compareOutput.setTimeusage(executeOutput.getTimeusage());
//			compareOutput.setMemoryusage(executeOutput.getMemoryusage());
			throw new JudgeException(compareOutput);
		}

	}

	/**
	 * 比對答案行是否相等，包含多重測資的比較
	 * 
	 * @return
	 */
	private boolean isEquals(int linenumber, String userline, String systemline) {
		userline = userline.trim();
		systemline = systemline.trim();
		if (userline.equals(systemline)) {
			return true;
		}

		return userline.equals(systemline);
	}

}
