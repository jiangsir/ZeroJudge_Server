/**
 * idv.jiangsir.Object - CompileOutput.java
 * 2011/7/25 下午10:48:47
 * nknush-001
 */
package tw.zerojudge.Server.Object;

import tw.zerojudge.Server.Beans.ServerOutput;

/**
 * @author nknush-001
 * 
 */
public class ExecuteOutput extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// public static String JUDGEMENT_RE = "RE";
	// public static String JUDGEMENT_TLE = "TLE";
	// public static String JUDGEMENT_MLE = "MLE";
	// public static String JUDGEMENT_OLE = "OLE";
	// public static String JUDGEMENT_SE = "SE";
	private ServerOutput.JUDGEMENT judgement = ServerOutput.JUDGEMENT.SE;
	private String info = "";

	private ServerOutput.REASON reason = ServerOutput.REASON.SYSTEMERROR;
	private int exitstatus = -1;
	private long timeusage = -1; // ms
	private int memoryusage = -1; // KB 改成 MB

	private String hint = null;

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
