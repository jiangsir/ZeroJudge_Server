/**
 * idv.jiangsir.Object - CompileOutput.java
 * 2011/7/25 下午10:48:47
 * nknush-001
 */
package tw.zerojudge.Server.Object;

import tw.zerojudge.Server.Beans.ServerOutput;

/**
 * @author jiangsir
 * 
 */
public class ExecuteOutput extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerOutput.JUDGEMENT judgement = ServerOutput.JUDGEMENT.SE;
	private String info = "";
	private String debug = ""; // debug 專用，使用者頁面不會顯示。 debugger 應該看到。
	private ServerOutput.REASON reason = ServerOutput.REASON.SYSTEMERROR;
	private int exitstatus = -1;
	private long timeusage = -1;
	private int memoryusage = -1;

	private String hint = "";

	public ServerOutput.JUDGEMENT getJudgement() {
		return judgement;
	}

	public void setJudgement(ServerOutput.JUDGEMENT judgement) {
		this.judgement = judgement;
	}

	public ServerOutput.REASON getReason() {
		return reason;
	}

	public void setReason(ServerOutput.REASON reason) {
		this.reason = reason;
	}

	public int getExitstatus() {
		return exitstatus;
	}

	public void setExitstatus(int exitstatus) {
		this.exitstatus = exitstatus;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public long getTimeusage() {
		return timeusage;
	}

	public void setTimeusage(long timeusage) {
		this.timeusage = timeusage;
	}

	public int getMemoryusage() {
		return memoryusage;
	}

	public void setMemoryusage(int memoryusage) {
		this.memoryusage = memoryusage;
	}

}
