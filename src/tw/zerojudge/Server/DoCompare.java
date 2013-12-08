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
import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.CompareInput;
import tw.zerojudge.Server.Object.CompareOutput;

/**
 * @author nknush-001
 * 
 */
public class DoCompare {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ServerInput serverInput;
	CompareInput compareInput;
	CompareOutput compareOutput = new CompareOutput();

	// String Path_Testdata = "/Testdata/";
	// String Path_Bin = "/Bin/";

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

		// System.out.println("doCompare: testfilename="
		// + compareInput.getTestfilename() + ", mode="
		// + compareInput.getMode());
		if (ServerInput.MODE.Tolerant == compareInput.getMode()) {
			compareOutput = this.TolerantComparison(systemout, userout);
			// serverOutput.setJudgement(output.getJudgement());
			// serverOutput.setInfo(output.getInfo());
			// serverOutput.setDetail(output.getDetail());
			return compareOutput;
		} else if (ServerInput.MODE.Strictly == compareInput.getMode()) {
			compareOutput = this.StrictlyComparison(systemout, userout);
			// serverOutput.setJudgement(output.getJudgement());
			// serverOutput.setInfo(output.getInfo());
			// serverOutput.setDetail(output.getDetail());
			return compareOutput;
		} else if (ServerInput.MODE.Special == compareInput.getMode()) {
			File useroutfile = new File(serverConfig.getTempPath(),
					compareInput.getCodename() + ".out");

			compareOutput = this.SpecialComparison(
					serverConfig.getTestdataPath() + File.separator
							+ compareInput.getTestfilename() + ".in",
					serverConfig.getTestdataPath() + File.separator
							+ compareInput.getTestfilename() + ".out",
					useroutfile);
			// serverOutput.setJudgement(output.getJudgement());
			// serverOutput.setInfo(output.getInfo());
			// serverOutput.setDetail(output.getDetail());
			// System.out.println("compareOutput=" +
			// compareOutput.getJudgement()
			// + compareOutput.getInfo());
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

	// /**
	// * 將 timeusage 專為適當顯示方式
	// *
	// * @param timeusage
	// * @return
	// */
	// public static String parseTimeusage(Long timeusage) {
	// return timeusage < 1000 ? timeusage + "ms" : new DecimalFormat(
	// "######.#").format(timeusage / 1000.0) + "s";
	// }
	//
	// /**
	// * 將 memoryusage 專為適當顯示方式
	// *
	// * @param memoryusage
	// * @return
	// */
	// public static String parseMemoryusage(Integer memoryusage) {
	// return memoryusage < 1024 ? memoryusage + "KB" : new DecimalFormat(
	// "######.#").format((int) memoryusage / 1024.0) + "MB";
	// }

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
						// System.out.println("userline 為 空行 略過");
					}
					while ("".equals(systemline)) {
						systemline = systemout.readLine();
						// System.out.println("systemline 為 空行 略過");
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
					// return new String[] { "WA", "line:" + count };
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
			// output.setInfo(parseTimeusage(compareInput.getTimeusage()) + ", "
			// + parseMemoryusage(compareInput.getMemoryusage()));
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
			// System.out.println("systemline=" + systemline);
			// System.out.println("userline=" + userline);
			if (userline == null || systemline == null) {
				break;
			}
			// 20100614 strictly 直接比較是否相等，不做其他修飾了。
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
			// output.setInfo(parseTimeusage(compareInput.getTimeusage()) + ", "
			// + parseMemoryusage(compareInput.getMemoryusage()));
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

	/**
	 * 條件式評分，交由出題者自行決定
	 * 
	 * @param judger
	 * @param systeminfile
	 * @param systemoutfile
	 * @param useroutfile
	 * @return
	 */
	private CompareOutput SpecialComparison(String systeminfile,
			String systemoutfile, File useroutfile) throws JudgeException {
		CompareOutput output = new CompareOutput();

		// 20100613 Special Judge 必須規定 judge 程式為 Special_a???.exe
		String judgecmd = serverConfig.getSpecialPath() + File.separator
				+ "Special_" + compareInput.getProblemid() + ".exe";
		File judgefile = new File(judgecmd);
		if (!judgefile.exists()) {
			judgecmd = serverConfig.getSpecialPath() + File.separator
					+ "Special_" + compareInput.getProblemid() + ".class";
			judgefile = new File(judgecmd);
			if (!judgefile.exists()) {
				output.setJudgement(ServerOutput.JUDGEMENT.SE);
				output.setHint("Special Judge 程式不存在！" + judgefile.getPath());
				throw new JudgeException(output);
			}
			// 20100613 代表這個題目的 Special Judge 是 java 寫成的
			judgecmd = "java -classpath " + serverConfig.getSpecialPath()
					+ File.separator + " Special_"
					+ compareInput.getProblemid();
		}

		judgecmd += " \"" + systeminfile + "\"";
		judgecmd += " \"" + systemoutfile + "\"";
		judgecmd += " \"" + useroutfile + "\"";
		String[] cmd = new String[] { "/bin/sh", "-c", judgecmd };
		System.out.println("Special Judge CMD = " + cmd[2]);
		RunCommand special = new RunCommand(cmd, 0);
		special.run();

		String returnline = "";
		String JUDGE_RESULT = "";
		// String SCORE = "";
		String MESSAGE = "";
		String CASES = "";
		String LINECOUNT = "";
		String USEROUT = "";
		String SYSTEMOUT = "";
		System.out.println("special.getErrorStream().size()="
				+ special.getErrorStream().size());
		System.out.println("special.getOutputStream().size()="
				+ special.getOutputStream().size());
		int linecount = special.getOutputStream().size();
		for (int i = 0; i < linecount; i++) {
			returnline = special.getOutputStream().get(i);
			System.out.println("returnline=" + returnline);
			if (returnline.startsWith("$JUDGE_RESULT=")) {
				JUDGE_RESULT = returnline
						.substring(returnline.indexOf("=") + 1);
			} else if (returnline.startsWith("$CASES=")) {
				CASES = returnline.substring(returnline.indexOf("=") + 1);
			} else if (returnline.startsWith("$LINECOUNT=")) {
				LINECOUNT = returnline.substring(returnline.indexOf("=") + 1);
			} else if (returnline.startsWith("$USEROUT=")) {
				USEROUT = returnline.substring(returnline.indexOf("=") + 1)
						+ "\n";
				while (i < linecount - 1
						&& !special.getOutputStream().get(i + 1).trim()
								.startsWith("$")) {
					USEROUT += special.getOutputStream().get(++i).trim() + "\n";
				}
				if (USEROUT.length() > 2000) {
					USEROUT = USEROUT.substring(0, 2000);
					USEROUT += "... USEROUT太長省略!";
				}
			} else if (returnline.startsWith("$SYSTEMOUT=")) {
				SYSTEMOUT = returnline.substring(returnline.indexOf("=") + 1)
						+ "\n";
				while (i < linecount - 1
						&& !special.getOutputStream().get(i + 1).trim()
								.startsWith("$")) {
					SYSTEMOUT += special.getOutputStream().get(++i).trim()
							+ "\n";
				}
				if (SYSTEMOUT.length() > 2000) {
					SYSTEMOUT = SYSTEMOUT.substring(0, 2000);
					SYSTEMOUT += "... SYSTEMOUT太長省略!";
				}
			} else if (returnline.startsWith("$MESSAGE=")) {
				MESSAGE = returnline.substring(returnline.indexOf("=") + 1)
						.trim() + "\n";

				while (i < linecount - 1
						&& !special.getOutputStream().get(i + 1).trim()
								.startsWith("$")) {
					MESSAGE += special.getOutputStream().get(++i).trim() + "\n";
				}
				if (MESSAGE.length() >= 2000) {
					MESSAGE = MESSAGE.substring(0, 2000);
					MESSAGE += "... MESSAGE太長省略!";
				}
			}
		}
		System.out.println("JUDGE_RESULT=" + JUDGE_RESULT);
		ServerOutput.JUDGEMENT RESULT = ServerOutput.JUDGEMENT
				.valueOf(JUDGE_RESULT);
		if (ServerOutput.JUDGEMENT.AC == RESULT) {
			output.setJudgement(RESULT);
			// output.setInfo(parseTimeusage(compareInput.getTimeusage()) + ", "
			// + parseMemoryusage(compareInput.getMemoryusage()));
			output.setReason(ServerOutput.REASON.AC);
			// output.setInfo(parseTimeusage(serverOutput.getTimeusage()) + ", "
			// + parseMemoryusage(serverOutput.getMemoryusage()));
			return output;
		} else if (ServerOutput.JUDGEMENT.WA == RESULT) {
			output.setJudgement(RESULT);

			// this.wa_errmsg += "與正確輸出不符合";
			if (!"".equals(CASES)) {
				output.setInfo("case:" + CASES);
				output.setReason(ServerOutput.REASON.ANSWER_NOT_MATCHED);
				output.setHint("與正確輸出不相符");
			}
			if (!"".equals(LINECOUNT)) {
				output.setInfo("line:" + LINECOUNT);
				// this.detailBean.setInfo("line:" + LINECOUNT);
				// this.wa_errmsg += "(line:" + LINECOUNT + ")";
				// result[1] = "line:" + LINECOUNT;
			}
			// this.wa_errmsg += "\n";
			if (!"".equals(MESSAGE)) {
				output.setHint(output.getHint() + MESSAGE);
			}
			if (!"".equals(USEROUT) && compareInput.isShowdetail()) {
				output.setHint(output.getHint() + "您的答案為：" + USEROUT);
			}
			if (!"".equals(SYSTEMOUT) && compareInput.isShowdetail()) {
				output.setHint(output.getHint() + "正確答案為：" + SYSTEMOUT);
			}
			// this.detailBean.setJudgement("WA");
			// output.setHint(output.());

			throw new JudgeException(output);
		}
		output.setJudgement(ServerOutput.JUDGEMENT.SE);
		output.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
		output.setHint("比對答案時，發生未定義錯誤！");
		throw new JudgeException(output);
	}

	/**
	 * 比對答案行是否相等，包含多重測資的比較
	 * 
	 * @return
	 */
	private boolean isEquals(int linenumber, String userline, String systemline) {
		// boolean result = false;
		// 判決的優先順序
		// 1. 條件式判決
		// 2. 直接比較
		// 3. 多重測資
		// if (specialjudge != null && !"".equals(specialjudge)) {
		// if (userline.equals(systemline)) {
		// return true;
		// } else {
		// return ConditionalJudger(specialjudge.trim(), userline
		// .trim(), systemline.trim());
		// }
		// }
		userline = userline.trim();
		systemline = systemline.trim();
		if (userline.equals(systemline)) {
			return true;
		}
		// 暫時不處理 alter out data 20110801

		// 主測資比對不成功才來比對 alter data
		// String alteroutdata = (judgeObject.getProblem().getAlteroutdata())
		// .trim();
		// if (alteroutdata == null || "".equals(alteroutdata)) {
		// return result;
		// }
		// String[] alterarray = alteroutdata.split("\\\n");
		// System.out.println("line number=" + linenumber);
		// for (int i = 0; i < alterarray.length; i++) {
		// int line = Integer.parseInt(alterarray[i].substring(0,
		// alterarray[i].indexOf(":")));
		// if (linenumber == line) {
		// String alterline = alterarray[i].substring(alterarray[i]
		// .indexOf(":") + 1);
		// System.out.println("line: " + line + " userline=" + userline
		// + " v.s. alterline=" + alterline);
		// result = userline.trim().equals(alterline.trim());
		// System.out.println("result=" + result);
		// }
		// if (result == true) {
		// return result;
		// }
		// System.out.println("alter answer" + i + "=" + alterarray[i]);
		// }
		// return result;
		return userline.equals(systemline);
	}

}
