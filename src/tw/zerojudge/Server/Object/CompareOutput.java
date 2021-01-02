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
public class CompareOutput extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String session_account;
	private ServerOutput.JUDGEMENT judgement = ServerOutput.JUDGEMENT.SE;
	private String info = "";
	private ServerOutput.REASON reason = ServerOutput.REASON.SYSTEMERROR;
	private int exitstatus = -1;
	private String hint = "";
	private long timeusage = -1;
	private int memoryusage = -1;

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

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
		int max_length = 3000;
		if (hint.length() <= max_length) {
			this.hint = hint;
		} else {
			this.hint = hint.substring(0, max_length - 1) + "...提示太長省略。";
		}
	}

	/**
	 * judgement 後的簡短訊息 如: WA (line:30)
	 * 
	 * @return
	 */
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
