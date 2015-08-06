/**
 * idv.jiangsir.Server - DoCompile.java
 * 2011/7/25 下午10:40:58
 * nknush-001
 */
package tw.zerojudge.Server;

import java.util.ArrayList;
import org.codehaus.jackson.map.ObjectMapper;

import quicktime.std.qtcomponents.SCInfo;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.*;

/**
 * @author jiangsir
 * 
 */
public class DoSpecialExecute {
	ExecuteInput executeInput;
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ObjectMapper mapper = new ObjectMapper();

	public DoSpecialExecute(ExecuteInput executeInput) {
		this.executeInput = executeInput;
	}

	public ExecuteOutput run() throws JudgeException {
		ExecuteOutput output = new ExecuteOutput();

		RunCommand special_execute = new RunCommand(executeInput.getCommand());
		special_execute.setTimelimit(executeInput.getTimelimit());
		special_execute.run();

		if (special_execute.getCause().getExitCode() != 0) {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPARE);
			output.setHint("Special Judge 裁判程式無法執行！("
					+ special_execute.getCause().getPlainMessage() + ")");
			throw new JudgeException(output);
		}
		output = this.getRusage(special_execute, output);
		output = this.getResult(special_execute, output);
		return output;
	}

	public static enum SPECIAL_RESULT {
		$JUDGE_RESULT, // AC or WA
		$CASES, // ? ：回報使用者錯誤發生在第幾個 test case，也可以不回報。
		$LINECOUNT, // ? ：回報使用者錯誤發生在第幾行，也可以不回報。
		$USEROUT, // xxx ：回報使用者所輸出的答案。
		$SYSTEMOUT, // xxx ：回報系統標準答案。
		$MESSAGE, // xxx：回報使用者相關訊息，訊息的詳細程度由出題者自行決定。
	}

	/**
	 * 
	 * @param special_execute
	 * @return
	 * @throws JudgeException
	 */
	private ExecuteOutput getResult(RunCommand special_execute,
			ExecuteOutput output) throws JudgeException {
		ArrayList<String> outputStream = special_execute.getOutputStream();
		// ExecuteOutput output = new ExecuteOutput();
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
			if (returnline.startsWith(SPECIAL_RESULT.$JUDGE_RESULT + "=")) {
				JUDGE_RESULT = returnline
						.substring(returnline.indexOf("=") + 1);
			} else if (returnline.startsWith(SPECIAL_RESULT.$CASES + "=")) {
				CASES = returnline.substring(returnline.indexOf("=") + 1);
			} else if (returnline.startsWith(SPECIAL_RESULT.$LINECOUNT + "=")) {
				LINECOUNT = returnline.substring(returnline.indexOf("=") + 1);
			} else if (returnline.startsWith(SPECIAL_RESULT.$USEROUT + "=")) {
				USEROUT = returnline.substring(returnline.indexOf("=") + 1)
						+ "\n";
				while (i < linecount - 1
						&& !outputStream.get(i + 1).trim().startsWith("$")) {
					USEROUT += outputStream.get(++i).trim() + "\n";
				}
				if (USEROUT.length() > 2000) {
					USEROUT = USEROUT.substring(0, 2000);
					USEROUT += "... " + SPECIAL_RESULT.$USEROUT + "太長省略!";
				}
			} else if (returnline.startsWith(SPECIAL_RESULT.$SYSTEMOUT + "=")) {
				SYSTEMOUT = returnline.substring(returnline.indexOf("=") + 1)
						+ "\n";
				while (i < linecount - 1
						&& !outputStream.get(i + 1).trim().startsWith("$")) {
					SYSTEMOUT += outputStream.get(++i).trim() + "\n";
				}
				if (SYSTEMOUT.length() > 2000) {
					SYSTEMOUT = SYSTEMOUT.substring(0, 2000);
					SYSTEMOUT += "... " + SPECIAL_RESULT.$SYSTEMOUT + "太長省略!";
				}
			} else if (returnline.startsWith(SPECIAL_RESULT.$MESSAGE + "=")) {
				MESSAGE = returnline.substring(returnline.indexOf("=") + 1)
						.trim() + "\n";

				while (i < linecount - 1
						&& !outputStream.get(i + 1).trim().startsWith("$")) {
					MESSAGE += outputStream.get(++i).trim() + "\n";
				}
				if (MESSAGE.length() >= 2000) {
					MESSAGE = MESSAGE.substring(0, 2000);
					MESSAGE += "... " + SPECIAL_RESULT.$MESSAGE + "太長省略!";
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
				output.setHint("與正確輸出不相符");
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
	private ExecuteOutput getRusage(RunCommand special_execute,
			ExecuteOutput output) throws JudgeException {
		long timeusage = -1;
		int memoryusage = -1;

		Rusage rusage = new Rusage(special_execute);
		System.out.println("rusage.getTime()=" + rusage.getTime());
		System.out.println("rusage.getMem()=" + rusage.getMem());
		if (rusage.getTime() >= 0) {
			timeusage = (long) ((rusage.getTime() + rusage.getBasetime())
					* 1000);
			output.setTimeusage(timeusage);
		}
		if (rusage.getMem() >= 0 && rusage.getBasemem() >= 0) {
			memoryusage = (rusage.getMem() - rusage.getBasemem())
					* rusage.getPagesize() / 1024;
			output.setMemoryusage(memoryusage);
		}
		if (timeusage > 0
				&& timeusage >= executeInput.getTimelimit() * 1000 * 0.95) {
			output.setJudgement(ServerOutput.JUDGEMENT.TLE);
			output.setTimeusage((long) (executeInput.getTimelimit() * 1000));
			System.out.println("Execute output=" + output.getTimeusage());
			output.setReason(ServerOutput.REASON.SPECIALJUDGE_EXECUTE_TLE);
			output.setHint(special_execute.getErrorString());
			throw new JudgeException(output);
		}

		if (memoryusage > 0
				&& memoryusage >= executeInput.getMemorylimit() * 1024) {
			output.setJudgement(ServerOutput.JUDGEMENT.MLE);
			output.setReason(ServerOutput.REASON.SPECIALJUDGE_EXECUTE_MLE);
			output.setHint(special_execute.getErrorString());
			throw new JudgeException(output);
		}

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
			System.out
					.println("output.getTimeusage()=" + output.getTimeusage());
			System.out.println(
					"output.getMemoryusage()=" + output.getMemoryusage());
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
			output.setHint(
					"系統呼叫了 abort 函式！\n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("135".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGBUS");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint(
					"嘗試定址不相符的記憶體位址。\n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else if ("136".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGFPE");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint(
					"溢位或者除以0的錯誤!! \n" + special_execute.getErrorString());
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
			output.setHint(
					"輸出檔大小超過規定上限 !! \n" + special_execute.getErrorString());
			throw new JudgeException(output);
		} else {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("執行時期未定義錯誤，code = " + rusage.getWEXITSTATUS() + " \n"
					+ special_execute.getErrorString());
			throw new JudgeException(output);
		}

	}

}
