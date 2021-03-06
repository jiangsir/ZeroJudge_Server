/**
 * idv.jiangsir.Server - DoCompare.java
 * 2011/7/25 下午10:42:54
 * nknush-001
 */
package tw.zerojudge.Server;

import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.ExecuteInput;
import tw.zerojudge.Server.Object.ExecuteOutput;
import tw.zerojudge.Server.Object.Rusage;

/**
 * @author jiangsir
 * 
 */
public class DoExecute {
	ExecuteInput executeInput;

	public DoExecute(ExecuteInput executeInput) {
		this.executeInput = executeInput;
	}

	public ExecuteOutput run() throws JudgeException {
		long timeusage = -1;
		int memoryusage = -1;
		ExecuteOutput output = new ExecuteOutput();
		String cmd = executeInput.getCommand();
		System.out.println(cmd);
		RunCommand execute = new RunCommand(
				new String[] { "/bin/sh", "-c", cmd }, 0);
		execute.setTimelimit(executeInput.getTimelimit());
		execute.run();

		Rusage rusage = new Rusage(execute);

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
			output.setReason(ServerOutput.REASON.TLE);
			output.setHint(execute.getErrorString());
			throw new JudgeException(output);
		}

		if (memoryusage > 0
				&& memoryusage >= executeInput.getMemorylimit() * 1024) {
			output.setJudgement(ServerOutput.JUDGEMENT.MLE);
			output.setReason(ServerOutput.REASON.MLE);
			output.setHint(execute.getErrorString());
			throw new JudgeException(output);
		}

		if (rusage.getWEXITSTATUS() == null) {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.FORCED_STOP);
			output.setHint(execute.getErrorString());
			throw new JudgeException(output);
		} else if ("1".equals(rusage.getWIFSIGNALED())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIG:" + rusage.getWTERMSIG());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("您的程式無法正常執行。\n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("0".equals(rusage.getWEXITSTATUS())) {
			return output;
		} else if ("1".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint(
					"您的程式被監控系統中斷，可能是程式無法正常結束所導致。\n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("127".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("無法分配記憶體。" + execute.getErrorString());
			throw new JudgeException(output);

		} else if ("132".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGILL");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("執行了非法的指令。\n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("134".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGABRT");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("系統呼叫了 abort 函式！\n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("135".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGBUS");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("嘗試定址不相符的記憶體位址。\n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("136".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGFPE");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("溢位或者除以0的錯誤!! \n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("137".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGKILL");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("產生立即終止訊號!! \n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("139".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGSEGV");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("記憶體區段錯誤！ \n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("143".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("SIGTERM");
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("產生程式中斷訊號！\n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if ("153".equals(rusage.getWEXITSTATUS())) {
			output.setJudgement(ServerOutput.JUDGEMENT.OLE);
			output.setInfo("SIGXFSZ");
			output.setReason(ServerOutput.REASON.OLE);
			output.setHint("輸出檔大小超過規定上限 !! \n" + execute.getErrorString());
			throw new JudgeException(output);
		} else if (("106".equals(rusage.getWEXITSTATUS())
				|| "200".equals(rusage.getWEXITSTATUS())
				|| "201".equals(rusage.getWEXITSTATUS())
				|| "202".equals(rusage.getWEXITSTATUS())
				|| "205".equals(rusage.getWEXITSTATUS())
				|| "207".equals(rusage.getWEXITSTATUS())
				|| "204".equals(rusage.getWEXITSTATUS())
				|| "216".equals(rusage.getWEXITSTATUS())
				|| "217".equals(rusage.getWEXITSTATUS()))
				&& "PASCAL".equals(executeInput.getLanguage())) {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("PASCAL 代碼(code:" + rusage.getWEXITSTATUS() + ")\n"
					+ execute.getErrorString());
			throw new JudgeException(output);
		} else {
			output.setJudgement(ServerOutput.JUDGEMENT.RE);
			output.setInfo("code:" + rusage.getWEXITSTATUS());
			output.setReason(ServerOutput.REASON.RE);
			output.setHint("執行時期未定義錯誤，code = " + rusage.getWEXITSTATUS() + " \n"
					+ execute.getErrorString());
			throw new JudgeException(output);
		}

	}
}
