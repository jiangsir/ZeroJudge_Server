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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Factories.ServerFactory;
import tw.zerojudge.Server.Object.CompareInput;
import tw.zerojudge.Server.Object.CompareOutput;
import tw.zerojudge.Server.Object.CompileOutput;
import tw.zerojudge.Server.Object.Rusage;

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
		try {
			FileInputStream system = new FileInputStream(new File(ConfigFactory
					.getServerConfig().getTestdataPath(),
					compareInput.getTestfilename() + ".out"));
			FileInputStream user = new FileInputStream(new File(
					serverConfig.getTempPath(), compareInput.getCodename()
							+ ".out"));
			systemout = new BufferedReader(new InputStreamReader(system,
					"UTF-8"));
			userout = new BufferedReader(new InputStreamReader(user, "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput
					.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
			compareOutput.setHint("未產生輸出檔。(" + e.getLocalizedMessage() + ")");
			throw new JudgeException(compareOutput);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput
					.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
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
			File useroutfile = new File(serverConfig.getTempPath(),
					compareInput.getCodename() + ".out");
			compareOutput = this.SpecialComparison(
					new File(serverConfig.getTestdataPath(), compareInput
							.getTestfilename() + ".in"),
					new File(serverConfig.getTestdataPath(), compareInput
							.getTestfilename() + ".out"), useroutfile);
			try {
				systemout.close();
				userout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return compareOutput;
		} else {
			compareOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			compareOutput.setHint("未知的 judge mode(" + compareInput.getMode()
					+ "), 必須是 寬鬆 or 嚴格 or 特殊 其中之一！");
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
	private CompareOutput TolerantComparison(BufferedReader systemout,
			BufferedReader userout) throws JudgeException {
		CompareOutput output = new CompareOutput();
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
						userline = userline.substring(0,
								(int) (systemline.length() + 6) - 1)
								+ " ...略";
					}
					output.setJudgement(ServerOutput.JUDGEMENT.WA);
					output.setReason(ServerOutput.REASON.ANSWER_NOT_MATCHED);
					output.setHint("您的答案為: " + this.parseLine(userline)
							+ "\n正確答案為: " + this.parseLine(systemline) + "\n");
					output.setInfo("line:" + count);
					throw new JudgeException(output);
				}
			} while (systemline != null && userline != null);
			userout.close();
			systemout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (userline == null && systemline != null) {
			output.setJudgement(ServerOutput.JUDGEMENT.WA);
			output.setInfo("line:" + count);
			output.setReason(ServerOutput.REASON.LESS_THAN_STANDARD_OUTPUT);
			output.setHint("您共輸出 " + (count - 1) + " 行。");
			throw new JudgeException(output);
		} else if (userline != null && systemline == null) {
			output.setJudgement(ServerOutput.JUDGEMENT.OLE);
			output.setInfo("line:" + count);
			output.setReason(ServerOutput.REASON.MORE_THAN_STANDARD_OUTPUT);
			output.setHint("請勿輸出題目未要求的文字: \n" + userline);
			throw new JudgeException(output);
		} else if (systemline == null && userline == null) {
			output.setJudgement(ServerOutput.JUDGEMENT.AC);
			output.setReason(ServerOutput.REASON.AC);
			output.setHint("");
			return output;
		} else {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SYSTEMERROR);
			output.setHint("寬鬆比對時發生未定義錯誤！");
			throw new JudgeException(output);
		}
	}

	/**
	 * 嚴格比對，就如同 UVa/ACM 一樣，只要有一個空格，空行不同就算錯誤。
	 */
	private CompareOutput StrictlyComparison(BufferedReader systemout,
			BufferedReader userout) throws JudgeException {
		CompareOutput output = new CompareOutput();
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
					userline = userline.substring(0,
							(int) (systemline.length() + 6) - 1) + " ...略";
				}

				output.setJudgement(ServerOutput.JUDGEMENT.WA);
				output.setReason(ServerOutput.REASON.ANSWER_NOT_MATCHED);
				output.setInfo("line:" + count);
				output.setHint("您的答案為: " + this.parseLine(userline)
						+ "\n正確答案為: " + this.parseLine(systemline) + "\n");
				throw new JudgeException(output);
			}
		} while (systemline != null && userline != null);

		if (userline == null && systemline != null) {
			output.setJudgement(ServerOutput.JUDGEMENT.WA);
			output.setInfo("line:" + count);
			output.setReason(ServerOutput.REASON.LESS_THAN_STANDARD_OUTPUT);
			output.setHint("您只輸出了 " + (count - 1) + " 行。");
			throw new JudgeException(output);
		} else if (userline != null && systemline == null) {
			output.setJudgement(ServerOutput.JUDGEMENT.OLE);
			output.setInfo("line:" + count);
			output.setReason(ServerOutput.REASON.MORE_THAN_STANDARD_OUTPUT);
			output.setHint("請勿輸出題目未要求的文字: \n" + userline);
			throw new JudgeException(output);
		} else if (systemline == null && userline == null) {
			output.setJudgement(ServerOutput.JUDGEMENT.AC);
			output.setReason(ServerOutput.REASON.AC);
			output.setHint("");
			return output;
		} else {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SYSTEMERROR);
			output.setHint("嚴格比對時發生未定義錯誤！");
			throw new JudgeException(output);
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
	private CompareOutput SpecialComparison(File systeminfile,
			File systemoutfile, File useroutfile) throws JudgeException {
		CompareOutput output = new CompareOutput();

		// String judgecmd = serverConfig.getSpecialPath(compareInput
		// .getProblemid())
		// + "Special_"
		// + compareInput.getProblemid()
		// + ".exe";
		File special_source = new File(serverConfig.getSpecialPath(compareInput
				.getProblemid()), "Special_" + compareInput.getProblemid()
				+ ".cpp");
		File special_exe = new File(special_source.toString().replaceAll(
				".cpp", ".exe"));
		// if (!judgefile.exists()) {
		// judgecmd = serverConfig.getSpecialPath() + File.separator
		// + compareInput.getProblemid() + File.separator + "Special_"
		// + compareInput.getProblemid() + ".class";
		// judgefile = new File(judgecmd);
		if (!special_source.exists()) {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SPECIAL_JUDGE_NOT_FOUND);
			output.setHint("Special Judge 原始程式不存在！" + special_source);
			throw new JudgeException(output);
		} else if (!special_exe.exists()) {
			try {
				new DoSpecialCompile(special_source, special_exe).run();
			} catch (JudgeException e) {
				e.printStackTrace();
				CompileOutput compileOutput = (CompileOutput) e.getCause();
				output.setJudgement(compileOutput.getJudgement());
				output.setInfo(compileOutput.getInfo());
				output.setReason(compileOutput.getReason());
				output.setHint(compileOutput.getHint());
				throw new JudgeException(output);
			}
		}
		try {
			output = new DoSpecialCompare(special_exe, systeminfile,
					systemoutfile, useroutfile).run();
			return output;
		} catch (JudgeException e) {
			e.printStackTrace();
			CompareOutput compareOutput = (CompareOutput) e.getCause();
			output.setJudgement(compareOutput.getJudgement());
			output.setInfo(compareOutput.getInfo());
			output.setReason(compareOutput.getReason());
			output.setHint(compareOutput.getHint());
			throw new JudgeException(output);
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
