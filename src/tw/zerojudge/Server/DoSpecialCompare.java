/**
 * idv.jiangsir.Server - DoCompile.java
 * 2011/7/25 下午10:40:58
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.*;

/**
 * @author jiangsir
 * 
 */
public class DoSpecialCompare {
	File special_exe;
	File systemin;
	File systemout;
	File userout;
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ObjectMapper mapper = new ObjectMapper();

	public DoSpecialCompare(File special_exe, File systemin, File systemout,
			File userout) {
		this.special_exe = special_exe;
		this.systemin = systemin;
		this.systemout = systemout;
		this.userout = userout;
	}

	public CompareOutput run() throws JudgeException {
		CompareOutput output = new CompareOutput();

		String cmd_special = serverConfig.getBinPath() + File.separator
				+ "shell.exe 10 640000000 100000000 \""
				+ serverConfig.getBinPath() + File.separator
				+ "base_cpp.exe\" \"" + special_exe + " \"" + systemin + "\""
				+ " \"" + systemout + "\"" + " \"" + userout + "\"" + "\"";

		RunCommand special_execute = new RunCommand(cmd_special);
		special_execute.run();

		if (special_execute.getCause().getExitCode() != 0) {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
			output.setHint("Special Judge 裁判程式無法執行！("
					+ special_execute.getCause().getPlainMessage() + ")");
			throw new JudgeException(output);
		}

		output = this.getRusage(special_execute);
		output = this.getResult(special_execute);
		return output;
	}

	/**
	 * 
	 * @param special_execute
	 * @return
	 * @throws JudgeException
	 */
	private CompareOutput getResult(RunCommand special_execute)
			throws JudgeException {
		ArrayList<String> outputStream = special_execute.getOutputStream();
		CompareOutput output = new CompareOutput();
		String returnline = "";
		String JUDGE_RESULT = "";
		String MESSAGE = "";
		String CASES = "";
		String LINECOUNT = "";
		String USEROUT = "";
		String SYSTEMOUT = "";
		int linecount = outputStream.size();
		for (int i = 0; i < linecount; i++) {
			returnline = outputStream.get(i);
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
						&& !outputStream.get(i + 1).trim().startsWith("$")) {
					USEROUT += outputStream.get(++i).trim() + "\n";
				}
				if (USEROUT.length() > 2000) {
					USEROUT = USEROUT.substring(0, 2000);
					USEROUT += "... USEROUT太長省略!";
				}
			} else if (returnline.startsWith("$SYSTEMOUT=")) {
				SYSTEMOUT = returnline.substring(returnline.indexOf("=") + 1)
						+ "\n";
				while (i < linecount - 1
						&& !outputStream.get(i + 1).trim().startsWith("$")) {
					SYSTEMOUT += outputStream.get(++i).trim() + "\n";
				}
				if (SYSTEMOUT.length() > 2000) {
					SYSTEMOUT = SYSTEMOUT.substring(0, 2000);
					SYSTEMOUT += "... SYSTEMOUT太長省略!";
				}
			} else if (returnline.startsWith("$MESSAGE=")) {
				MESSAGE = returnline.substring(returnline.indexOf("=") + 1)
						.trim() + "\n";

				while (i < linecount - 1
						&& !outputStream.get(i + 1).trim().startsWith("$")) {
					MESSAGE += outputStream.get(++i).trim() + "\n";
				}
				if (MESSAGE.length() >= 2000) {
					MESSAGE = MESSAGE.substring(0, 2000);
					MESSAGE += "... MESSAGE太長省略!";
				}
			}
		}
		ServerOutput.JUDGEMENT RESULT;
		try {
			RESULT = ServerOutput.JUDGEMENT.valueOf(JUDGE_RESULT);
		} catch (Exception e) {
			e.printStackTrace();
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
			output.setHint("Special Judge 回報不正確！("
					+ special_execute.getOutputString() + ")");
			throw new JudgeException(output);
		}

		if (ServerOutput.JUDGEMENT.AC == RESULT) {
			output.setJudgement(RESULT);
			output.setReason(ServerOutput.REASON.AC);
			return output;
		} else if (ServerOutput.JUDGEMENT.WA == RESULT) {
			output.setJudgement(RESULT);
			output.setReason(ServerOutput.REASON.ANSWER_NOT_MATCHED);

			if (!"".equals(CASES)) {
				output.setInfo("case:" + CASES);
				// output.setHint("與正確輸出不相符");
			}
			if (!"".equals(LINECOUNT)) {
				output.setInfo("line:" + LINECOUNT);
			}
			if (!"".equals(MESSAGE)) {
				output.setHint(output.getHint() + MESSAGE);
			}
			// if (!"".equals(USEROUT) && compareInput.isShowdetail()) {
			// output.setHint(output.getHint() + "您的答案為：" + USEROUT);
			// }
			// if (!"".equals(SYSTEMOUT) && compareInput.isShowdetail()) {
			// output.setHint(output.getHint() + "正確答案為：" + SYSTEMOUT);
			// }

			throw new JudgeException(output);
		}
		output.setJudgement(ServerOutput.JUDGEMENT.SE);
		output.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
		output.setHint("比對答案時，發生未定義錯誤！");
		throw new JudgeException(output);
	}

	/**
	 * 
	 * @param special_execute
	 * @return
	 * @throws JudgeException
	 */
	private CompareOutput getRusage(RunCommand special_execute)
			throws JudgeException {
		CompareOutput output = new CompareOutput();

		Rusage rusage = new Rusage(special_execute.getOutputStream(),
				special_execute.getErrorString());
		if (rusage.getWEXITSTATUS() == null) {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.FORCED_STOP);
			output.setHint(special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("1".equals(rusage.getWIFSIGNALED())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIG:" + rusage.getWTERMSIG());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("您的程式無法正常執行。\n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("0".equals(rusage.getWEXITSTATUS())) {
			return output;
		} else if ("1".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("您的程式被監控系統中斷，可能是程式無法正常結束所導致。\n"
					+ special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("127".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("無法分配記憶體。" + special_execute.getErrorString());
			throw new JudgeException(output);

		} else if ("132".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGILL");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("執行了非法的指令。\n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("134".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGABRT");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("系統呼叫了 abort 函式！\n"
					+ special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("135".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGBUS");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("嘗試定址不相符的記憶體位址。\n"
					+ special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("136".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGFPE");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("溢位或者除以0的錯誤!! \n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("137".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGKILL");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("產生立即終止訊號!! \n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("139".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGSEGV");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("記憶體區段錯誤！ \n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("143".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGTERM");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("產生程式中斷訊號！\n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("153".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.OLE);
			output.setInfo("SIGXFSZ");
			output.setReason(ServerOutput.REASON.OLE);
			output.setHint("輸出檔大小超過規定上限 !! \n"
					+ special_execute.getErrorString());
			throw new JudgeException(output);
		} else {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("執行時期未定義錯誤，code = " + rusage.getWEXITSTATUS()
					+ " \n" + special_execute.getErrorString());
			throw new JudgeException(output);
		}

	}

}
